## 基本的文件 I/O

我想 open, read, write, lseek, close 这个几个操作就满足了对文件操作的基本需求。当然了，我也是看书是这么写的。

每个语言基本都有对应的函数或方法，我们调用就行，在这种情况下，我们可以理解成 -> `语言就是个工具`。我比较偏向 Go 的风格，所以这里我以 Go 的函数库为例，但在介绍其之前，要明白一个概念：文件描述符。

画中重点了：

对于内核而言, 所有打开的文件都通过文件描述符引用。文件描述符是一个非负整数。

对上面的描述还是有点模糊呢？

当打开一个现有文件或创建一个新的文件时，内核向进程返回一个 `文件描述符`。

当读、写一个文件时，使用 open 或 create 返回的 `文件描述符` 标识该文件，将 `文件描述符` 作为参数传递给 read 或 write。 

通常用变量 `fd` 来表示文件描述符 (file descripter)

### 函数 open 和 openat & 函数 create

调用 open 或 openat 函数就可以打开或创建一个文件。

``` c
#include <fcntl.h>

int open(const char *path, int oflag, ... /* mode_t mode */);

int openat(int fd, const char *path, int oflag, ... /* mode_t mode */);
```

调用 create 函数创建一个新文件。

``` go
#include <fcntl.h>

int create(const char *path, mode_t mode);
```

上面函数中的参数：
- path 是要打开或创建文件的名字
- oflag 是对文件进行哪些操作的 flag, 例如：O_RDWR|O_CREATE|O_TRUNC
- mode 指定该文件的访问权限位
- fd 表示文件描述符

在这里罗列了 Go 中对文件进行哪些操作的 flags:

``` go
// Flags to OpenFile wrapping those of the underlying system. Not all
// flags may be implemented on a given system.
const (
	O_RDONLY int = syscall.O_RDONLY // open the file read-only.
	O_WRONLY int = syscall.O_WRONLY // open the file write-only.
	O_RDWR   int = syscall.O_RDWR   // open the file read-write.
	O_APPEND int = syscall.O_APPEND // append data to the file when writing.
	O_CREATE int = syscall.O_CREAT  // create a new file if none exists.
	O_EXCL   int = syscall.O_EXCL   // used with O_CREATE, file must not exist
	O_SYNC   int = syscall.O_SYNC   // open for synchronous I/O.
	O_TRUNC  int = syscall.O_TRUNC  // if possible, truncate file when opened.
)
```

如何用 Go 打开或创建一个文件：

``` go 
// Open file 
func Open(name string) (*File, error) {
	return OpenFile(name, O_RDONLY, 0)
}

// Create file 
func Create(name string) (*File, error) {
	return OpenFile(name, O_RDWR|O_CREATE|O_TRUNC, 0666)
}
```

通过观察源码，得知二者都是调用 `OpenFile` 函数，只是 flag, mode 不同。

``` go
// OpenFile is the generalized open call; most users will use Open
// or Create instead. It opens the named file with specified flag
// (O_RDONLY etc.) and perm, (0666 etc.) if applicable. If successful,
// methods on the returned File can be used for I/O.
// If there is an error, it will be of type *PathError.
func OpenFile(name string, flag int, perm FileMode) (*File, error) {
	chmod := false
	if !supportsCreateWithStickyBit && flag&O_CREATE != 0 && perm&ModeSticky != 0 {
		if _, err := Stat(name); IsNotExist(err) {
			chmod = true
		}
	}

	var r int
	for {
		var e error
		r, e = syscall.Open(name, flag|syscall.O_CLOEXEC, syscallMode(perm))
		if e == nil {
			break
		}

		// On OS X, sigaction(2) doesn't guarantee that SA_RESTART will cause
		// open(2) to be restarted for regular files. This is easy to reproduce on
		// fuse file systems (see http://golang.org/issue/11180).
		if runtime.GOOS == "darwin" && e == syscall.EINTR {
			continue
		}

		return nil, &PathError{"open", name, e}
	}

	// open(2) itself won't handle the sticky bit on *BSD and Solaris
	if chmod {
		Chmod(name, perm)
	}

	// There's a race here with fork/exec, which we are
	// content to live with. See ../syscall/exec_unix.go.
	if !supportsCloseOnExec {
		syscall.CloseOnExec(r)
	}

	return newFile(uintptr(r), name), nil
}
```

当读上面这段代码时，`supportsCreatedWithStickyBit` 这就卡住啦，知识点就是 `StickyBit` （粘着位）

了解下 `StickyBit` （粘着位）：

在 UNIX 还没有使用请求分页式技术的早期版本中，如果 `可执行文件` 设置了 `StickyBit`，在执行该文件结束时，程序的正文部分的一个副本仍被保存在交换区，以便下次执行时，可以迅速装入内存。然而现今的 UNIX 中大多数配置了虚拟存储系统以及快速文件系统，所以不再需要使用该技术啦。

在 `OpenFile` 函数源码中, 常量`supportsCreatedWithStickyBit` 在 Ubuntu 16.04 环境下的值是 true, 故那部分代码不会被执行。所以在 Ubuntu 16.04 环境下的开发者可以不用去了解 `if !supportsCreatedWithStickyBit ... ` 代码块。由于使用 Ubuntu 16.04 的缘故，所以 `OpenFile` 函数可以简化如下：

``` go
// OpenFile is the generalized open call; most users will use Open
// or Create instead. It opens the named file with specified flag
// (O_RDONLY etc.) and perm, (0666 etc.) if applicable. If successful,
// methods on the returned File can be used for I/O.
// If there is an error, it will be of type *PathError.
func OpenFile(name string, flag int, perm FileMode) (*File, error) {
	var r int
	for {
		var e error
		r, e = syscall.Open(name, flag|syscall.O_CLOEXEC, syscallMode(perm))
		if e == nil {
			break
		}
		return nil, &PathError{"open", name, e}
	}
	return newFile(uintptr(r), name), nil
}
```

简化后的代码，发现核心代码就是：`syscall.Open(name, flag|syscall.O_CLOEXEC, syscallMode(perm))`, 触发系统调用。在深入了解之前，咱先把 `syscallMode(prem)` 解决掉，扫除障碍。

``` go
// syscallMode returns the syscall-specific mode bits from Go's portable mode bits.
func syscallMode(i FileMode) (o uint32) {
	o |= uint32(i.Perm())
	if i&ModeSetuid != 0 {
		o |= syscall.S_ISUID
	}
	if i&ModeSetgid != 0 {
		o |= syscall.S_ISGID
	}
	if i&ModeSticky != 0 {
		o |= syscall.S_ISVTX
	}
	// No mapping for Go's ModeTemporary (plan9 only).
	return
}
```

让我们了解下 `FileMode`,源码是这样定义的 `type FileMode uint32`, 并通过查看源码得值 `i.Perm()` 等价于 `i & 0777`, 并通过了解 `Open` 的 mode 为 0 ，syscallMode(0) == 0 ;`Create` 中 mode 为 0666, syscallMode(0666) == 438

> Tips: 一开始因为 posix 结尾的文件是 “posix系统” (不存在的) 下调用的，查了之后，才知道是 unix 系统下调用的。

那让我们关注点切换到 `syscall.Open(name, mode, prem)` 上, 类似 c 中的方法吧！深度的话先挖到这个地方。

让我们回到简化后的 `OpenFile` 剩余的知识点: `PathError`, `NewFile(uintptr(r), name)`。

`PathError` 的源码如下：

``` go
// PathError records an error and the operation and file path that caused it.
type PathError struct {
	Op   string
	Path string
	Err  error
}

func (e *PathError) Error() string { return e.Op + " " + e.Path + ": " + e.Err.Error() }
```

`error` 是个接口, 只要实现了 `Error` 方法就 OK.

`uintptr(r)` 中 `uintptr` 定义如下：

``` go
// uintptr is an integer type that is large enough to hold the bit pattern of
// any pointer.
type uintptr uintptr
```

`uintptr(r)` 中 `r` 是个 `int` 类型。

看下 `NewFile` 这个函数是怎么定义的，源码如下：

``` go
// NewFile returns a new File with the given file descriptor and name.
func NewFile(fd uintptr, name string) *File {
	fdi := int(fd)
	if fdi < 0 {
		return nil
	}
	f := &File{&file{fd: fdi, name: name}}
	runtime.SetFinalizer(f.file, (*file).close)
	return f
}
```

上面函数中 `fd` 经过一轮回又回到了 `int` 类型。`File` 是 `file` 类型的封装，源码如下：

``` go
// File represents an open file descriptor.
type File struct {
	*file // os specific
}

// file is the real representation of *File.
// The extra level of indirection ensures that no clients of os
// can overwrite this data, which could cause the finalizer
// to close the wrong file descriptor.
type file struct {
	fd      int
	name    string
	dirinfo *dirInfo // nil unless directory being read
}
```

上面函数中 `runtime.SetFinalizer(f.file, (*file).close)`, 类型 c/c++ 中的 `析构函数` 吧！(挖, 先这吧)

### 函数 close 

调用 `close` 函数关闭一个打开文件。

``` c
#include <unistd.h>

int close(int fd);
```

如何用 Go 来关闭一个文件呢？

``` go
// Close closes the File, rendering it unusable for I/O.
// It returns an error, if any.
func (f *File) Close() error {
	if f == nil {
		return ErrInvalid
	}
	return f.file.close()
}

func (file *file) close() error {
	if file == nil || file.fd == badFd {
		return syscall.EINVAL
	}
	var err error
	if e := syscall.Close(file.fd); e != nil {
		err = &PathError{"close", file.name, e}
	}
	file.fd = -1 // so it can't be closed again

	// no need for a finalizer anymore
	runtime.SetFinalizer(file, nil)
	return err
}
```

从上面的代码中可见，`syscall.Close(file.fd)` 类似 `c` 中的 `close`，起着关键性的作用。其源码如下：

``` go
// THIS FILE IS GENERATED BY THE COMMAND AT THE TOP; DO NOT EDIT

func Close(fd int) (err error) {
	_, _, e1 := Syscall(SYS_CLOSE, uintptr(fd), 0, 0)
	if e1 != 0 {
		err = errnoErr(e1)
	}
	return
}
```

`Syscall(SYS_CLOSE, uintptr(fd), 0, 0)` 估计是更底层的调用了，就不再挖啦。

### 函数 lseek

调用 lseek 显式地为一个打开文件设置偏移量。

``` c
#include <unistd.h>

off_t lseek(int fd, off_t offset, int whence);
```

上面函数中的参数：
- fd 表示文件描述符
- 若 whence 是 SEEK_SET, 则将该文件的偏移量设置为`距文件开始处 offset` 个字节 
- 若 whence 是 SEEK_CUR, 则将该文件的偏移量设置为`其当前值加 offset, offset `可正可负
- 若 whence 是 SEEK_END, 则将该文件的偏移量设置为`文件长度加 offset, offset `可正可负

这些参数是在 Go 也适用的, 但是这种方式，已经在 Go 中弃用啦，详情如下：

``` go
// Seek whence values.
//
// Deprecated: Use io.SeekStart, io.SeekCurrent, and io.SeekEnd.
const (
	SEEK_SET int = 0 // seek relative to the origin of the file
	SEEK_CUR int = 1 // seek relative to the current offset
	SEEK_END int = 2 // seek relative to the end
)
```

如何用 Go 来设置文件的偏移量呢？

``` go
// Seek sets the offset for the next Read or Write on file to offset, interpreted
// according to whence: 0 means relative to the origin of the file, 1 means
// relative to the current offset, and 2 means relative to the end.
// It returns the new offset and an error, if any.
// The behavior of Seek on a file opened with O_APPEND is not specified.
func (f *File) Seek(offset int64, whence int) (ret int64, err error) {
	if err := f.checkValid("seek"); err != nil {
		return 0, err
	}
	r, e := f.seek(offset, whence)
	if e == nil && f.dirinfo != nil && r != 0 {
		e = syscall.EISDIR
	}
	if e != nil {
		return 0, f.wrapErr("seek", e)
	}
	return r, nil
}
```

可见 `f.seek(offset, whence)` 起着关键性的作用。

``` go
// seek sets the offset for the next Read or Write on file to offset, interpreted
// according to whence: 0 means relative to the origin of the file, 1 means
// relative to the current offset, and 2 means relative to the end.
// It returns the new offset and an error, if any.
func (f *File) seek(offset int64, whence int) (ret int64, err error) {
	return syscall.Seek(f.fd, offset, whence)
}
```

`syscall.Seek(f.fd, offset, whence)` 发起了一个系统调用，再挖就到了再底层和汇编啦。

### 函数 read

调用 read 函数从打开文件中读取数据。

``` c
#include <unistd.h>

ssize_t read(int fd, void *buf, size_t nbytes);
```

上面函数中的参数：

- fd 表示文件描述符
- buf 要读的文件，类型是通用的指针
- nbytes 表示读取的字节数

如果 read 成功, 则返回读到的字节数，如已到达文件的尾端，则返回 0。

Tips: 有多种情况可能使实际读到的字节数少于要求读的字节数。

如何用 Go 从打开文件中读取数据呢？

``` go
// Read reads up to len(b) bytes from the File.
// It returns the number of bytes read and any error encountered.
// At end of file, Read returns 0, io.EOF.
func (f *File) Read(b []byte) (n int, err error) {
	if err := f.checkValid("read"); err != nil {
		return 0, err
	}
	n, e := f.read(b)
	return n, f.wrapErr("read", e)
}
```

其底层代码如上，递归查看 `go package`。

### 函数 write

调用 write 函数向打开文件写数据。

``` c 
#include <unistd.h>

ssize_t write(int fd, const void *buf, size_t nbytes);
```

上面函数的参数:

- fd 表示文件描述符
- buf 要写的文件，类型是通用的指针
- nbytes 表示读取的字节数

如果 write 成功, 则返回读到的字节数，如已到达文件的尾端，则返回 0。


如何用 Go 向打开文件中写入数据？

``` go 
func (f *File) Write(b []byte) (n int, err error)
```

### 结束

如果光看 APUE, 前几页还可以，慢慢就看不下去了，Go 的 lib 基本跟 unix 的接口相似，就结合着 Go 的源码一起看了，只要有个大概的框架就 OK, 随着往后慢慢深入，会有更深的理解。

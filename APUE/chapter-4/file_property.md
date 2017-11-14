## 文件属性

当我们想获取有关文件信息的时候，该怎么办？ 当然是调用 `stat` 函数啦。

在 APUE 中给出了 4 个 `stat`函数如下所示：

``` c
#include <sys/stat.h>

int stat(const char *restrict pathname, struct stat *restrict buf);

int fstat(int fd, struct stat *buf);

int lstat(const char *restrict pathname, struct stat *restrict buf);

int fstatat(int fd, const char *restrict pathname, struct stat *restrict buf, int flag);
```

上面函数中的参数：
- pathname 表示文件地址
- fd 表示文件描述符 
- buf 表示一个指针，指向我们提供的结构体
- flag 表示是否跟随着一个符号链接

主要 Get 到 `stat` 函数用于获取文件结构信息, 就好。

那么用 Go 来获取下文件结构信息：

``` go
func Stat(name string) (FileInfo, error)
```

上面函数中的参数：
- name 表示文件地址
- FileInfo 表示文件结构信息

上面的 `Stat` 函数位于 `os` 包下，重要就是 `FileInfo` 这个数据结构，源码如下：

``` go
type FileInfo interface {
        Name() string       // base name of the file
        Size() int64        // length in bytes for regular files; system-dependent for others
        Mode() FileMode     // file mode bits
        ModTime() time.Time // modification time
        IsDir() bool        // abbreviation for Mode().IsDir()
        Sys() interface{}   // underlying data source (can return nil)
}
```

上面的基本类型都不陌生，需要看看 `FileMode` 的类型：

``` go
type FileMode uint32

// The defined file mode bits are the most significant bits of the FileMode.
// The nine least-significant bits are the standard Unix rwxrwxrwx permissions.
// The values of these bits should be considered part of the public API and may be 
// used in wire protocols or disk representations: they must not be changed,
// although new bits might be added.
const (
        // The single letters are the abbreviations
        // used by the String method's formatting.
        ModeDir        FileMode = 1 << (32 - 1 - iota) // d: is a directory
        ModeAppend                                     // a: append-only
        ModeExclusive                                  // l: exclusive use
        ModeTemporary                                  // T: temporary file; Plan 9 only
        ModeSymlink                                    // L: symbolic link
        ModeDevice                                     // D: device file
        ModeNamedPipe                                  // p: named pipe (FIFO)
        ModeSocket                                     // S: Unix domain socket
        ModeSetuid                                     // u: setuid
        ModeSetgid                                     // g: setgid
        ModeCharDevice                                 // c: Unix character device, when ModeDevice is set
        ModeSticky                                     // t: sticky

        // Mask for the type bits. For regular files, none will be set.
        ModeType = ModeDir | ModeSymlink | ModeNamedPipe | ModeSocket | ModeDevice

        ModePerm FileMode = 0777 // Unix permission bits
)
```

这段代码引出好几个知识点，罗列如下：
- 文件类型 
- 文件模式字(st_mode)
- 文件访问权限

#### 文件类型

1. 普通文件（regular file)，包含各种数据的文件。
2. 目录文件	(directory file), 原来目录也是一种文件。
3. 块特殊文件 (block special file), 这种类型的文件提供对设备带缓冲的访问 (没用过)。
4. 字符特殊文件 (character special file), 这种类型的文件不提供对设备带缓冲的访问。
5. FIFO 这种文件用于进程间通信。
6. 套接字（socket), 这种用于进程间的网络通信。
7. 符号链接 (symbolicc link)，这种文件指向另一文件。

#### 文件模式字(st_mode)

如上代码中的常量定义，可知：

1. ModeDir    ---> 目录文件
2. Symlink    ---> 符号链接文件
3. Device     ---> 块特殊文件 (猜)
4. NamePipe   ---> FIFO
5. Socket     ---> 套接字
6. CharDevice ---> 字符特殊文件

有关 `普通文件` 查看了 Go 标准库，代码如下：

``` go 
// IsRegular reports whether m describes a regular file.
// That is, it tests that no mode type bits are set.
func (m FileMode) IsRegular() bool {
	return m&ModeType == 0
}
```

!ModeType ---> 普通文件

#### 文件访问权限

需要了解标准的 Unix 权限 `rwxrwxrwx`, 也是 `ModePerm` 这个 Value, `rwxrwxrwx` 分别对应着 `owner`, `group`, `other`。  

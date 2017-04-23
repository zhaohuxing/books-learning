# Java 多线程编程核心技术
> 声明:junit不支持多线程测试,故现弃用.仅提供部分重要代码,省略验证类型代码,建立知识框架
## 第一章 Java多线程技能
- thread/src/main/java/com/sprint/ch01/下所以文件
- thread/src/test/java/com/sprint/ch01/下所以文件

![多线程基础.jpg](http://upload-images.jianshu.io/upload_images/2031765-dd8c04aedbdaaeb4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 第二章 对象及变量的并发访问
 
![synchronizedMethod.jpg](http://upload-images.jianshu.io/upload_images/2031765-292516d632cbf4d1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


synchronized同步代码块(170423)
 - 一半异步,一半同步:不在synchronized中执行的异步,在synchronized中执行的同步 
 - synchronized代码块间的同步性: 当多个线程访问同一个object中的synchronized代码块时, 最前得到object锁的线程,先执行对应synchronized中的代码,其他执行想执行synchronized代码块的线程进入阻塞状态.必须等待锁释放后才能获取.
 - synchronized(this)锁定是当前对象: synchronized代码块间的同步性的原因吧
 - synchronized(anyObject)锁定其他对象: 打破synchronized代码块间的同步性,提高效率.这时的anyObject,应在方法内部声明,因方法内的变量是线程安全的
 - 静态同步synchronized方法与synchronized(class)代码块:二者作用一样,对Class进行加锁,而Class锁可以对类的所以对象实例起作用
 - 避免使用String作为锁对象: String常量有缓存的作用,要避免这种情况带来的问题,建议使用new Object()的方式
 - 多线程的死锁: 线程永远得不到锁,相互进行等待

![synchronizedBlock.jpg](http://upload-images.jianshu.io/upload_images/2031765-b3a9ba1f37e9839e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




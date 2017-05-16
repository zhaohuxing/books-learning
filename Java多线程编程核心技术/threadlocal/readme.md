类ThreadLocal的作用:主要解决每个线程绑定自己的值，用于共享变量的在多线程中的分离。<br />
- VariableIsolation, DateVariableIsolation.java 体现出ThreadLocal的作用
- ThreadLocalExt.java 继承ThreadLocal,可以设置初始值

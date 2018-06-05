# Channel

## 理解
channel理解为一种导管，他可以直接和底层的IO设备相连，channel的继承体系

![image](https://user-images.githubusercontent.com/10506633/40414290-6b682618-5eaa-11e8-9596-b58673e97de5.png)


```java
package java.nio.channels;
public interface Channel
{
public boolean isOpen( );
public void close( ) throws IOException;
}
```

与缓冲区不同，通道 API 主要由接口指定。不同的操作系统上通道实现（Channel
Implementation）会有根本性的差异，所以通道 API 仅仅描述了可以做什么。因此很自然地，通道
实现经常使用操作系统的本地代码。通道接口允许您以一种受控且可移植的方式来访问底层的 I/O
服务。

InterruptibleChannel 是一个标记接口，当被通道使用时可以标示该通道是可以中断的
（Interruptible）。如果连接可中断通道的线程被中断，那么该通道会以特别的方式工作，关于这一
点我们会在 3.1.3 节中进行讨论。大多数但非全部的通道都是可以中断的。

从 Channel 接口引申出的其他接口都是面向字节的子接口，包括 Writable ByteChannel 和
ReadableByteChannel。

## 打开通道

I/O 可以分为广义的两大类别：File I/O 和 Stream I/O。
它们是文件（file）通道和套接字（socket）通道。
有一个 FileChannel 类和三个 socket 通道类：SocketChannel、ServerSocketChannel 和 DatagramChannel。

```java
SocketChannel sc = SocketChannel.open( );
sc.connect (new InetSocketAddress ("somehost", someport));
ServerSocketChannel ssc = ServerSocketChannel.open( );
ssc.socket( ).bind (new InetSocketAddress (somelocalport));
DatagramChannel dc = DatagramChannel.open( );
RandomAccessFile raf = new RandomAccessFile ("somefile", "r");
FileChannel fc = raf.getChannel( );
```
### 使用通道
```java
public interface ReadableByteChannel extends Channel{
      public int read (ByteBuffer dst) throws IOException;
}
public interface WritableByteChannel extends Channel{
      public int write (ByteBuffer src) throws IOException;
}
public interface ByteChannel extends ReadableByteChannel, WritableByteChannel{
}
```

通道可以是单向（unidirectional）或者双向的（bidirectional）。一个 channel 类可能实现定义
read( )方法的 ReadableByteChannel 接口，而另一个 channel 类也许实现 WritableByteChannel 接口以
提供 write( )方法。实现这两种接口其中之一的类都是单向的，只能在一个方向上传输数据。如果
一个类同时实现这两个接口，那么它是双向的，可以双向传输数据。

ByteChannel 接口，该接口引申出了 ReadableByteChannel 和WritableByteChannel 两个接口。ByteChannel 接口本身并不定义新的 API 方法，它是一种用来聚集它自己以一个新名称继承的多个接口的便捷接口。根据定义，实现 ByteChannel 接口的通道会同时实现 ReadableByteChannel 和 WritableByteChannel 两个接口，所以此类通道是双向的;

通道会连接一个特定 I/O 服务且通道实例（channel instance）的性能受它所连接的 I/O 服务的特征限制.

 参考
```java
com.bnzxking.learning.nio.java.BufferCopy
```
这个实例中充分说明了读写怎么处理缓冲区的问题，一种是使用compact保证数据不丢失，另外一种是使用hasRemaining保证读取了所以的缓冲区数据。

### 关闭通道

```java
package java.nio.channels;
public interface Channel
{
public boolean isOpen( );
public void close( ) throws IOException;
}
```
调用通道的close( )方法时，可能会导致在通道关闭底层I/O服务的过程中线程暂时阻塞 ，哪怕该通道处于非阻塞模式。通道关闭时的阻塞行为（如果有的话）是高度取决于操作系统或者文件系统的。在一个通道上多次调用close( )方法是没有坏处的，但是如果第一个线程在close( )方法中阻塞，那么在它完成关闭通道之前，任何其他调用close( )方法都会**阻塞**。后续在该已关闭的通道上调用close( )不会产生任何操作，只会立即返回。

如果一个通道实现 InterruptibleChannel 接口它的行为以下述语义为准：如果一个线程在一个通道上被阻塞并且同时被中断（由调用该被阻塞线程的 interrupt( )方法的另一个线程中断），那么该通道将被关闭，该被阻塞线程也会产生一个 ClosedByInterruptException 异常。此外，假如一个线程的 interrupt status 被设置并且该线程试图访问一个通道，那么这个通道将立即被关闭，同时将抛出相同的 ClosedByInterruptException 异常。

***这种类似于传染的处理方式，就是可能通道的访问线程有很多，但是当其中一个线程被设置了中断状态，那么被他访问的通道都会被关闭。***

可中断的通道也是可以异步关闭的。实现 InterruptibleChannel 接口的通道可以在任何时候被关，即使有另一个被阻塞的线程在等待该通道上的一个 I/O 操作完成。当一个通道被关闭时，休眠在该通道上的所有线程都将被唤醒并接收到一个 AsynchronousCloseException 异常。接着通道就被关闭并将不再可用。

查看实例
```
com.bnzxking.learning.nio.java.CloseTest
```

### Scatter/Gather

通道提供了一种被称为 Scatter/Gather 的重要新功能。Scatter/Gather是一个简单却强大的概念，它是指在多个缓冲区上实现一个简单的 I/O 操作。对于一个 write 操作而言，数据是从几个缓冲区按顺序抽取（称为 gather）并沿着通道发送的。缓冲区本身并不需要具备这种 gather 的能力（通常它们也没有此能力）。该 gather 过程的效果就好比全部缓冲区的内容被连结起来，并在发送数据前存放到一个大的缓冲区中。对于 read 操作而言，从通道读取的数据会按顺序被散布（称为 scatter）到多个缓冲区，将每个缓冲区填满直至通道中的数据或者缓冲区的最大空间被消耗完。

大多数现代操作系统都支持本地矢量 I/O（native vectored I/O）。当您在一个通道上请求一个Scatter/Gather 操作时，该请求会被翻译为适当的本地调用来直接填充或抽取缓冲区。这是一个很大的进步，因为减少或避免了缓冲区拷贝和系统调用。Scatter/Gather 应该使用直接的 ByteBuffers 以从本地 I/O 获取最大性能优势。

```java
public interface ScatteringByteChannel extends ReadableByteChannel{
   public long read (ByteBuffer [] dsts) throws IOException;
   public long read (ByteBuffer [] dsts, int offset, int length) throws IOException;
}
public interface GatheringByteChannel extends WritableByteChannel{
   public long write(ByteBuffer[] srcs) throws IOException;
   public long write(ByteBuffer[] srcs, int offset, int length)throws IOException;
}
```

数据从缓冲区阵列引用的每个缓冲区中 gather 并被组合成沿着通道发送的字节流。

![image](https://user-images.githubusercontent.com/10506633/40833527-b56e7970-65c0-11e8-8e75-33cd27281426.png)


从通道传输来的数据被 scatter 到所列缓冲区，依次填充每个缓冲区（从缓冲区的 position 处开始到 limit 处结束）

![image](https://user-images.githubusercontent.com/10506633/40833583-e1e50546-65c0-11e8-8a53-8f381107fd2e.png)

带 offset 和 length 参数版本的 read( ) 和 write( )方法使得我们可以使用缓冲区阵列的子集缓冲区。
***这里的 offset 值指哪个缓冲区将开始被使用，而不是指数据的 offset***。

参考实例
```
com.bnzxking.learning.nio.java.Marketing
```
### 文件通道

文件通道总是阻塞式的，因此不能被置于非阻塞模式。现代操作系统都有复杂的缓存和预取机制，使得本地磁盘 I/O 操作延迟很少。对于文件 I/O，最强大之处在于异步 I/O（asynchronous I/O），它允许一个进程可以从操作系统请求一个或多个 I/O 操作而不必等待这些操作的完成。发起请求的进程之后会收到它请求的 I/O 操作已完成的通知。

FileChannel 对象是线程安全（thread-safe）的。多个进程可以在同一个实例上并发调用方法而不会引起任何问题，不过并非所有的操作都是多线程的（multithreaded）。影响通道位置或者影响文件大小的操作都是单线程的（single-threaded）。如果有一个线程已经在执行会影响通道位置或文件大小的操作，那么其他尝试进行此类操作之一的线程必须等待。并发行为也会受到底层的操作系统或文件系统影响。

API
```java
public abstract class FileChannel extends AbstractChannel implements ByteChannel, GatheringByteChannel, ScatteringByteChannel{
   public abstract long position( )
   public abstract void position (long newPosition)
   public abstract int read (ByteBuffer dst)
   public abstract int read (ByteBuffer dst, long position) 
   public abstract int write (ByteBuffer src)
   public abstract int write (ByteBuffer src, long position)
   public abstract long size( )
   public abstract void truncate (long size)
   public abstract void force (boolean metaData)
}
```

FileChannel 位置（position）是从底层的文件描述符获得的，该 position 同时被作为通道引用获取来源的文件对象共享。这也就意味着一个对象对该 position 的更新可以被另一个对象看到;

如果是同一个文件流打开的通道，那么position是可以及时更新的。
查看实例 ```com.bnzxking.learning.nio.java.FilePostionSharedTest```

同样类似于缓冲区，也有带 position 参数的绝对形式的 read( )和 write( )方法。这种绝对形式
的方法在返回值时不会改变当前的文件 position。由于通道的状态无需更新，因此绝对的读和写可
能会更加有效率，操作请求可以直接传到本地代码。更妙的是，多个线程可以并发访问同一个文件
而不会相互产生干扰。这是因为每次调用都是原子性的（atomic），并不依靠调用之间系统所记住
的状态。

***所以说read和write是不会修改position的状态的***

![image](https://user-images.githubusercontent.com/10506633/40835969-aa7e90e8-65c7-11e8-832b-031efb2a4b2d.png)

truncate( )方法会砍掉您所指定的新 size 值之外的所有数据。如果当前 size 大于新 size，超出新 size 的所有字节都会被悄悄地丢弃。如果提供的新 size 值大于或等于当前的文件 size 值，该文件不会被修改。这两种情况下，truncate( )都会产生副作用：文件的position 会被设置为所提供的新 size 值。

 force( )该方法告诉通道强制将全部待定的修改都应用到磁盘的文件上。所有的现代文件系统都会缓存数据和延迟磁盘文件更新以提高性能。调用 force( )方法要求文件的所有待定修改立即同步到磁盘。

force( )方法的布尔型参数表示在方法返回值前文件的元数据（metadata）是否也要被同步更新到磁盘。大多数情形下，该信息对数据恢复而言是不重要的。给 force( )方法传递 false 值表示在方法返回前只需要同步文件数据的更改。大多数情形下，同步元数据要求操作系统进行至少一次额外的底层 I/O 操作。一些大数量事务处理程序可能通过在每次调用 force( )方法时不要求元数据更新来获取较高的性能提升，同时也不会牺牲数据完整性。

### 文件锁定

有关 FileChannel 实现的文件锁定模型的一个重要注意项是：锁的对象是文件而不是通道或线程，这意味着文件锁不适用于判优同一台 Java 虚拟机上的多个线程发起的访问。

如果一个线程在某个文件上获得了一个独占锁，然后第二个线程利用一个单独打开的通道来请
求该文件的独占锁，那么第二个线程的请求会被批准。但如果这两个线程运行在不同的 Java 虚拟
机上，那么第二个线程会阻塞，因为锁最终是由操作系统或文件系统来判优的并且几乎总是在进程
级而非线程级上判优。

API
```java
public abstract class FileChannel extends AbstractChannel implements ByteChannel, GatheringByteChannel, ScatteringByteChannel{
   public final FileLock lock( )
   public abstract FileLock lock (long position, long size,  boolean shared)
   public final FileLock tryLock( )
   public abstract FileLock tryLock (long position, long size,boolean shared)
}
```

如果您正请求的锁定范围是有效的，那么 lock( )方法会阻塞，它必须等待前面的锁被释放。假如您的线程在此情形下被暂停，该线程的行为受中断语义控制。如果通道被另外一个线程关闭，该暂停线程将恢复并产生一个 AsynchronousCloseException 异常。假如该暂停线程被直接中断（通过调用它的 interrupt( )方法），它将醒来并产生一个FileLockInterruptionException 异常。如果在调用 lock( )方法时线程的 interrupt status 已经被设置，也会产生 FileLockInterruptionException 异常。两个名为 tryLock( )的方法，它们是 lock( )方法的非阻塞变体.

lock( )和 tryLock( )方法均返回一个 FileLock 对象.
FileLock API
```java
public abstract class FileLock
{
79
public final FileChannel channel( )
public final long position( )
public final long size( )
public final boolean isShared( )
public final boolean overlaps (long position, long size)
public abstract boolean isValid( );
public abstract void release( ) throws IOException;
}
```
- FileLock 类封装一个锁定的文件区域。FileLock 对象由 FileChannel 创建并且总是关联到那个特
  定的通道实例。您可以通过调用 channel( )方法来查询一个 lock 对象以判断它是由哪个通道创建
  的。
- 一个 FileLock 对象创建之后即有效，直到它的 release( )方法被调用或它所关联的通道被关闭或
  Java 虚拟机关闭时才会失效。我们可以通过调用 isValid( )布尔方法来测试一个锁的有效性。一个锁
  的有效性可能会随着时间而改变，不过它的其他属性——位置、范围大小和独占性——在创建时即被确定，不会随着时间而改变
- 您可以通过调用 overlaps( )方法来查询一个 FileLock 对象是否与一个指定的文件区域重
  叠。这将使您可以迅速判断您拥有的锁是否与一个感兴趣的区域（region of interest）有交叉。

1. 如果是在同一个java虚拟机中的线程，两个不同的线程lock区域重叠了，可以抛出OverlappingFileLockException异常
    ```com.bnzxking.learning.nio.java.FileLockTest```

2. 如果是在不同一个java虚拟机中的线程，两个不同的线程lock区域重叠了，后面一个线程会被阻塞
    ``` com.bnzxking.learning.nio.java.FileLockTest3 ```

  

  ### 内存映射文件

  新的 FileChannel 类提供了一个名为 map( )的方法，该方法可以在一个打开的文件和一个特殊类型的 ByteBuffer 之间建立一个虚拟内存映射.

  通过内存映射机制来访问一个文件会比使用常规方法读写高效得多，甚至比使用通道的效率都高。因为不需要做明确的系统调用，那会很消耗时间。更重要的是，操作系统的虚拟内存可以自动缓存内存页（memory page）。这些页是用系统内存来缓存的，所以不会消耗 Java 虚拟机内存堆（memory heap）。
  一旦一个内存页已经生效（从磁盘上缓存进来），它就能以完全的硬件速度再次被访问而不需要再次调用系统命令来获取数据。那些包含索引以及其他需频繁引用或更新的内容的巨大而结构化文件能因内存映射机制受益非常多。如果同时结合文件锁定来保护关键区域和控制事务原子性，那您将能了解到内存映射缓冲区如何可以被很好地利用。

  

  API:

  ```java
  public abstract class FileChannel extends AbstractChannel implements ByteChannel, GatheringByteChannel, ScatteringByteChannel{
  	public abstract MappedByteBuffer map (MapMode mode, long position,long size)
  	public static class MapMode{
          public static final MapMode READ_ONLY
          public static final MapMode READ_WRITE
          public static final MapMode PRIVATE
  	}
  }
  ```

  

  map的使用和之前的position很相似，不同的是如果size超过文件大小，那么文件会膨胀。

  内存映射的模式有三种，特别的模式有一种**PRIVATE** ，这种模式相当于使用了copy-on-write，当修改文件是，系统会copy出一份副本作为私有副本使用。这个时候，每个线程之间是不会有影响的。但是如果没有修改文件或是**没有修改的对应页**，那么对应的副本依然是可以共享的。

  

  参考实例

  ```com.bnzxking.learning.nio.java.MapFile```

  

###      Channel-to-Channel 传输

​	由于经常需要从一个位置将文件数据批量传输到另一个位置，FileChannel 类添加了一些优化
	方法来提高该传输过程的效率：

```java
public abstract class FileChannel extends AbstractChannel implements 			ByteChannel,GatheringByteChannel, ScatteringByteChannel{
    public abstract long transferTo (long position, long count,ritableByteChannel target)
    public abstract long transferFrom (ReadableByteChannel src,long position, long count)
}
```



### Socket 通道

新的 socket 通道类可以***运行非阻塞模式并且是可选择的***。这两个性能可以激活大程序巨大的可伸缩性和灵活性。本节中我们会看到，再也没有为每个 socket 连接使用一个线程的必要了，也避免了管理大量线程所需的上下文交换总开销。借助新的 NIO 类，一个或几个线程就可以管理成百上千的活动 socket 连接了并且只有很少甚至可能没有性能损失。

![1528185726648](https://i.imgur.com/nYqikR0.png)


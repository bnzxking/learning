## 属性

- 容量（Capacity）
- 上界（Limit）
- 位置（Position）
- 标记（Mark）

### 填充

![image](https://user-images.githubusercontent.com/10506633/40176206-27024a68-5a0d-11e8-9032-efb6d3f9ed79.png)

### 反转
![image](https://user-images.githubusercontent.com/10506633/40176214-355a2b62-5a0d-11e8-8ef8-479db2856293.png)

### 释放
      position = 0，limit=0；

### 压缩
```
public abstract class ByteBuffer
extends Buffer implements Comparable
{
// This is a partial API listing
public abstract ByteBuffer compact( );
}
```
 压缩前：
![image](https://user-images.githubusercontent.com/10506633/40211483-8f98bde4-5a7d-11e8-9f46-f6b7594a10cc.png)


压缩后（2-6的位置移到了 0-4,**并且模式从读切换为写**）：
![image](https://user-images.githubusercontent.com/10506633/40211487-942f477e-5a7d-11e8-9806-783925d6413d.png)



### 相等

- 个对象类型相同。包含不同数据类型的 buffer 永远不会相等，而且 buffer绝不会等于非 buffer 对象。
- 两个对象都剩余同样数量的元素。Buffer 的容量不需要相同，而且缓冲区中剩余数据的索引也不必相同。但每个缓冲区中剩余元素的数目（从位置到上界）必须相同。
- 在每个缓冲区中应被 Get()函数返回的剩余数据元素序列必须一致。




两个被认为是相等的缓冲区
![image](https://user-images.githubusercontent.com/10506633/40211495-a3f55482-5a7d-11e8-9295-91d1e10f8eaf.png)

两个被认为不相等的缓冲区
![image](https://user-images.githubusercontent.com/10506633/40211501-a6fc379a-5a7d-11e8-99a3-c30ff5b7bb43.png)


### 比较


1. 第一个不相等的元素小于另一个Buffer中对应的元素 。
2. 所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)。


### 批量操作
`com.bnzxking.learning.nio.java.BufferBatchOper`

### 创建缓冲区
`CharBuffer charbuffer = CharBuffer.wrap (myArray, 12, 42);`
创建了一个 position 值为 12，limit 值为 54，容量为 myArray.length 的缓冲
区。
实际上charbuffer的底层还是用完了整个myArray，12和42只是初始状态，可以参考`com.bnzxking.learning.nio.java.BufferWrap`

array() 返回真实的底层数组
arrayOffset() 返回缓冲区数据在数组中存储的开始位置的偏移量(数组切分)

### 复制
```
public abstract CharBuffer duplicate( );
public abstract CharBuffer asReadOnlyBuffer( );
public abstract CharBuffer slice( );
```
duplicate出来的缓冲区 除了共享底层数组之外，还会初始化复制体的信息

```
CharBuffer buffer = CharBuffer.allocate (8);
buffer.position (3).limit (6).mark( ).position (5);
CharBuffer dupeBuffer = buffer.duplicate( );
buffer.clear( );
```
![image](https://user-images.githubusercontent.com/10506633/40211506-acbdb028-5a7d-11e8-896e-3558898fb9eb.png)


mark、position、limit都复制过来了


slice就是共享数据需要切割，查看`com.bnzxking.learning.nio.java.BufferCopy`

### ByteBuffer

      因为操纵系统，硬件的数据交流都是byte方式，所以ByteBuffer有一些优势

* 直接字节缓冲区通常是 I/O 操作最好的选择。在设计方面，它们支持 JVM 可用的最高效
    I/O 机制。非直接字节缓冲区可以被传递给通道，但是这样可能导致性能损耗。通常非直接缓
    冲不可能成为一个本地 I/O 操作的目标。如果您向一个通道中传递一个非直接 ByteBuffer
    对象用于写入，通道可能会在每次调用中隐含地进行下面的操作:
    * 创建一个临时的直接 ByteBuffer 对象。
    * 将非直接缓冲区的内容复制到临时缓冲中。
    * 使用临时缓冲区执行低层次 I/O 操作。
    * 临时缓冲区对象离开作用域，并最终成为被回收的无用数据。
***
* 不好的地方：直接缓冲区时 I/O 的最佳选择，但可能比创建非直接缓冲区要花费更高的成本。直接缓
    冲区使用的内存是通过调用本地操作系统方面的代码分配的，绕过了标准 JVM 堆栈。建立和
    销毁直接缓冲区会明显比具有堆栈的缓冲区更加破费，这取决于主操作系统以及 JVM 实现。
    直接缓冲区的内存区域不受无用存储单元收集支配，因为它们位于标准 JVM 堆栈之外。

  直接字节缓冲区的申请：
```
   public static ByteBuffer allocateDirect (int capacity)
   public abstract boolean isDirect( );
```

### 视图缓冲区
可以通过 `asXXXBuffer`这一类API创建其他基本类型的Buffer，他们的特点是共享底层的数据，但是有自己独特的position和limit

![image](https://user-images.githubusercontent.com/10506633/40299869-3836cf88-5d1a-11e8-8344-07beb817773e.png)


```
ByteBuffer byteBuffer =
ByteBuffer.allocate (7).order (ByteOrder.BIG_ENDIAN);
CharBuffer charBuffer = byteBuffer.asCharBuffer( );
```
**这里有一个细节，就是从x86cpu读取的字节是小端的，但是java默认使用大端，所以要手动制定一下**

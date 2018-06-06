##filter执行原理 (bitSet机制和caching机制)

### 根据检索词 在 倒排索引中 获取document list
````
document    doc1  doc2  doc3
2018-06-07   *      *    *
2018-06-08          *    *
2018-06-09   *           *

检索: 2018-06-09  doc1 doc2 doc3
````

### 构建 bitSet
````
2进制的数组
2018-06-09  [1,0,1]

1:匹配
0:不匹配

2进制数组的好处
1. 简单的数据结构实现复杂的功能
2. 节省内存空间
3. 提升性能
````

###多个 filter, 遍历每个条件对应的bitSet  优先最稀疏的开始搜索

````
一次性在一个search中,发出多个filter条件
遍历所有的filter条件的 bitSet
优先最稀疏的

示例: 
[0,0,1,1,0] 1最少的
[0,1,1,1,1]

好处:
先遍历稀疏的,过滤掉尽可能多的数据

示例: 
filter1:postDate=2017-01-01
filter2:userID=1
postDate: [0, 0, 1, 1, 0, 0]  
userID:   [0, 1, 0, 1, 0, 1] 
最后结果就是 doc4
````

###caching bitSet
````
在最近的256个query中超过一定次数的过滤条件,缓存其bitSet
对于segment (<1000或<3%) 不缓存

缓存bitSet的好处:
例: 2018-06-09 搜索了10次缓存其BitSet 
下次同样的filter
不需要重新扫描倒排索引
反复生成bitSet,提升性能

1. filter针对小segment获取到的结果，可以不缓存
2. segment记录数<1000，
3. 或者segment大小<index总大小的3%

其不缓存的原因:
小的segment重新扫描也很快
合并之后,缓存没有意义,找不到了

````

### filter 和 query
````
filter 能进行 bitSet的缓存

filter大部分情况下 在 query之前执行

query: 计算doc对搜索条件的relevance score，还会根据这个score去排序
     : 计算score,根据 score排序
filter: 过滤想要的数据,不计算score     
````

### bitSet的自动更新
````
postDate=2017-01-01，[0, 0, 1, 0]

新增一条数据 
{id:3,postDate:2017-01-01}
[0, 0, 1, 0 , 1]

修改一条数据
{id:1,postDate:2017-01-01}
[1, 0, 1, 0 , 1]

````

###以后只要有相同的filter条件,使用caching bitSet

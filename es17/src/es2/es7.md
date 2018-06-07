##控制全文检索结果的精准度

````准备测试数据````

<pre><code>
POST /es2_7/test1/_bulk
{ "index": { "_id": 1 }}  
{ "articleID" : "XHDK-A-1293-#fJ3", "userID" : 1, "hidden": false, "postDate": "2017-01-01","tags": ["java", "hadoop"]}  
{ "index": { "_id": 2 }}  
{ "articleID" : "KDKE-B-9947-#kL5", "userID" : 1, "hidden": false, "postDate": "2017-01-02", "tags":["java"] }  
{ "index": { "_id": 3 }}  
{ "articleID" : "JODL-X-1937-#pV7", "userID" : 2, "hidden": false, "postDate": "2017-01-01","tags":["hadoop"]}  
{ "index": { "_id": 4 }}  
{ "articleID" : "QQPX-R-3956-#aD8", "userID" : 2, "hidden": true, "postDate": "2017-01-02", "tags":["java", "elasticsearch"]} 
{ "index": { "_id": 5}}  
{ "articleID" : "QQPX-R-3956-#aDF", "userID" : 2, "hidden": true, "postDate": "2017-01-23", "tags":["java", "elasticsearch"]}
</code></pre>

``添加字段 title``
<pre><code>
POST /es2_7/test1/_bulk
{ "update": { "_id": "1"} }
{ "doc" : {"title" : "this is java and elasticsearch blog"} }
{ "update": { "_id": "2"} }
{ "doc" : {"title" : "this is java blog"} }
{ "update": { "_id": "3"} }
{ "doc" : {"title" : "this is elasticsearch blog"} }
{ "update": { "_id": "4"} }
{ "doc" : {"title" : "this is java, elasticsearch, hadoop blog"} }
{ "update": { "_id": "5"} }
{ "doc" : {"title" : "this is spark blog"} }
</code></pre>


####全文检索 
```
1. filter 查询 : 过滤条件
2. term 查询 : exact value查询
3. match查询 : 全文检索, 如果字段是not_analyzed,或者keyword
            exact value 查询
下例查询: or查询            
````
<pre><code>
GET /es2_7/test1/_search
{
  "query": {
    "match": {
      "title": "java elasticsearch"
    }
  }
}
</code></pre>

####使用 operator 控制查询
````
where  java and elasticsearch 
````
<pre><code>
GET /es2_7/test1/_search
{
  "query": {
    "match": {
      "title":{
        "query": "java elasticsearch",
        "operator": "and"
      }
    
    }
  }
}
</code></pre>

####包含 百分比 个数 检索
<pre><code>
GET /es2_7/test1/_search
{
  "query": {
    "match": {
      "title": {
        "query": "java elasticsearch spark hadoop",
        "minimum_should_match":"75%"
      }
    }
  }
}
</code></pre>

####bool 组合多条件查询
<pre><code>
GET /es2_7/test1/_search?explain
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "java"
          }
        }
      ],
      "must_not": [
        {
          "match": {
            "title": "spark"
          }
        }
      ],
      "should": [
        {
          "match": {
            "title": "hadoop"
          }},
          {"match": {
            "title": "elasticsearch"
          }
            
          }
      ]
    }
  }
}
</code></pre>


#### bool 组合多条件查询 计算 relevance score
````
计算 must 和shoud 的分数和 / must 和 should 的个数

should 可以影响 relevance score 
1.must 是必须匹配
2.should 是可以匹配 也可以不匹配
3.should 匹配更多 相关分数 越高
````


####should 使用的注意
````
1. 有must 和 should , 不匹配也行
2. 只有should , 至少匹配一个才能返回结果
3. should 的精确匹配
````
<pre><code>
GET /es2_7/test1/_search
{
  "query": {
    "bool": {
      "should": [
       { "match": { "title": "java" }},
        { "match": { "title": "elasticsearch"   }},
        { "match": { "title": "hadoop"   }},
	      { "match": { "title": "spark"   }}
      ],
      "minimum_number_should_match": 2
    }
  }
}
</code></pre>
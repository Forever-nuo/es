``准备测试数据``
<pre><code>
POST /es2_2/test1/_bulk
{ "index": { "_id": 1 }}  
{ "articleID" : "XHDK-A-1293-#fJ3", "userID" : 1, "hidden": false, "postDate": "2017-01-01" }  
{ "index": { "_id": 2 }}  
{ "articleID" : "KDKE-B-9947-#kL5", "userID" : 1, "hidden": false, "postDate": "2017-01-02" }  
{ "index": { "_id": 3 }}  
{ "articleID" : "JODL-X-1937-#pV7", "userID" : 2, "hidden": false, "postDate": "2017-01-01" }  
{ "index": { "_id": 4 }}  
{ "articleID" : "QQPX-R-3956-#aD8", "userID" : 2, "hidden": true, "postDate": "2017-01-02" }  
</code></pre>

````
根据userID 查询数据
1. 对检索文本不分词,直接到倒排索引中去匹配
2. "hello world" 进行分词 "hello","world" 到 倒排索引中进行搜索
3. term, "hello world" 直接到 倒排索引中搜索
````
<pre><code>
GET /es2_2/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "userID": "1"
        } 
      }
    }
  }
}
</code></pre>

``搜索隐藏的帖子``
<pre><code>
GET /es2_2/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "hidden": true
        }
      },
      "boost": 1.2
    }
  }
}
</code></pre>

``根据发帖日期进行搜索``
<pre><code>
GET /es2_2/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "postDate": "2017-01-02"
        }
      },
      "boost": 1.2
    }
  }
}
</code></pre>

``根据articleID 进行检索``
<pre><code>
GET /es2_2/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "articleID": "QQPX-R-3956-#aD8"
        }
      }
    }
  }
}
</code></pre>

````
根据articleID.keyword 进行检索
1. articleID.keyword，是es最新版本内置建立的field，就是不分词的。
2. 所以一个articleID过来的时候，会建立两次索引，一次是自己本身，是要分词的，分词后放入倒排索引；
3. 另外一次是基于articleID.keyword，不分词，保留256个字符最多，直接一个字符串放入倒排索引中。
4. 所以term filter，对text过滤，可以考虑使用内置的field.keyword来进行匹配。
5. 但是有个问题，默认就保留256个字符。
6. 所以尽可能还是自己去手动建立索引，指定not_analyzed吧。
7. 在最新版本的es中，不需要指定not_analyzed也可以，将type=keyword即可。
````
<pre><code>
GET /es2_2/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "articleID.keyword": "QQPX-R-3956-#aD8"
        }
      }
    }
  }
}
</code></pre>

``查看分词--只能对tokened field进行查看分词效果``
<pre><code>
GET /es2_2/_analyze
{
  "field": "articleID",
  "text": "QQPX-R-3956-#aD8"
}
</code></pre>


``重建索引``
<pre><code>
DELETE  /es2_2

PUT /es2_2
{
  "mappings": {
    "test1" :{
      "properties": {
        "articleID" : {
          "type": "keyword"
        }
      }
    }
  }
}
</code></pre>

``重新添加测试数据``
<pre><code>
PUT /es2_2/test1/_bulk
{ "index": { "_id": 1 }}  
{ "articleID" : "XHDK-A-1293-#fJ3", "userID" : 1, "hidden": false, "postDate": "2017-01-01" }  
{ "index": { "_id": 2 }}  
{ "articleID" : "KDKE-B-9947-#kL5", "userID" : 1, "hidden": false, "postDate": "2017-01-02" }  
{ "index": { "_id": 3 }}  
{ "articleID" : "JODL-X-1937-#pV7", "userID" : 2, "hidden": false, "postDate": "2017-01-01" }  
{ "index": { "_id": 4 }}  
{ "articleID" : "QQPX-R-3956-#aD8", "userID" : 2, "hidden": true, "postDate": "2017-01-02" }
</code></pre>


``根据articleID 查询``
<pre><code>
GET /es2_2/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "articleID": "QQPX-R-3956-#aD8"
        }
      }
    }
  }
}
</code></pre>

``知识梳理``
1. term filter：根据exact value进行搜索，数字、boolean、date天然支持
2. text需要建索引时指定为not_analyzed，才能用term query
3. 相当于SQL中的单个where条件

select *
from forum.article
where articleID='XHDK-A-1293-#fJ3'

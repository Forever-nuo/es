## query match 转换为 bool term 


###准备测试数据
 <pre><code>
POST /es2_8/test1/_bulk
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




POST /es2_8/test1/_bulk
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

### 普通的 query match

``普通的query match ``
<pre><code>
GET /es2_8/test1/_search
{
  "query": {
    "match": {
      "title": "java elasticsearch"
    }
  }
}
</code></pre>

``should term``
<pre><code>
GET /es2_8/test1/_search
{
  "query": {
    "bool": {
      "should": [
        {"term": {"title": "java" }  },
          {"term": {"title": "elasticsearch" }  }
      ]
    }
  }
}
</code></pre>

### query match  operator

``query operator``
<pre><code>
GET /es2_8/test1/_search
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
``match term``
<pre><code>
GET /es2_8/test1/_search
{
  "query": {
    "bool": {
      "must": [
         {"term": {"title": "java" }  },
          {"term": {"title": "elasticsearch" }  }
      ]
    }
  }
}
</code></pre>

### query  match  minunum 

``query match minunum``
<pre><code>
GET /es2_8/test1/_search
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

``should term minunum``
<pre><code>
GET /es2_8/test1/_search
{
  "query": {
    "bool": {
      "should": [
        {"term":{"title":"java"}},
        {"term":{"title":"elasticsearch"}},
        {"term":{"title":"spark"}},
        {"term":{"title":"hadoop"}}
      ],
      "minimum_number_should_match": 3
    }
  }
}
</code></pre>
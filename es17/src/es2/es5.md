###terms 的使用

####准备数据
<pre><code>
POST /es2_5/test1/_bulk
{ "index": { "_id": 1 }}  
{ "articleID" : "XHDK-A-1293-#fJ3", "userID" : 1, "hidden": false, "postDate": "2017-01-01","tags": ["java", "hadoop"]}  
{ "index": { "_id": 2 }}  
{ "articleID" : "KDKE-B-9947-#kL5", "userID" : 1, "hidden": false, "postDate": "2017-01-02", "tags":["java"] }  
{ "index": { "_id": 3 }}  
{ "articleID" : "JODL-X-1937-#pV7", "userID" : 2, "hidden": false, "postDate": "2017-01-01","tags":["hadoop"]}  
{ "index": { "_id": 4 }}  
{ "articleID" : "QQPX-R-3956-#aD8", "userID" : 2, "hidden": true, "postDate": "2017-01-02", "tags":["java", "elasticsearch"} 
</code></pre>

####查询 JODL-X-1937-#pV7或 KDKE-B-9947-#kL5 
<pre><code>
GET /es2_5/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "terms": {
          "articleID.keyword": [
            "JODL-X-1937-#pV7",
            "KDKE-B-9947-#kL5"
          ]
        }
      }
    }
  }
}
</code></pre>

#### tag有java的
<pre><code>
GET /es2_5/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "terms": {
          "tags": [
            "java"
          ]
        }
      }
    }
  }
 }
</code></pre>

#### tag 只有java的

``优化document``
<pre><code>
POST /es2_5/test1/_bulk
{ "update": { "_id": "1"} }  
{ "doc" : {"tag_cnt" : 2} }  
{ "update": { "_id": "2"} }  
{ "doc" : {"tag_cnt" : 1} }  
{ "update": { "_id": "3"} }  
{ "doc" : {"tag_cnt" : 1} }  
{ "update": { "_id": "4"} }  
{ "doc" : {"tag_cnt" : 2} }  
</code></pre>

``查询``
<pre><code>
GET /es2_5/test1/_search
{
  "query": {
    "constant_score": {
        "filter": {
          "bool": {
            "must":[
            {"term":{"tag_cnt":1}},
            {"terms":{"tags":["java"]}}
          ]
        }
      }}
    }
}
</code></pre>
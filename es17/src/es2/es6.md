###range 的使用

``准备测试数``
<pre><code>
POST /es2_6/test1/_bulk
{ "index": { "_id": 1 }}  
{ "articleID" : "XHDK-A-1293-#fJ3", "userID" : 1, "hidden": false, "postDate": "2017-01-01","tags": ["java", "hadoop"]}  
{ "index": { "_id": 2 }}  
{ "articleID" : "KDKE-B-9947-#kL5", "userID" : 1, "hidden": false, "postDate": "2017-01-02", "tags":["java"] }  
{ "index": { "_id": 3 }}  
{ "articleID" : "JODL-X-1937-#pV7", "userID" : 2, "hidden": false, "postDate": "2017-01-01","tags":["hadoop"]}  
{ "index": { "_id": 4 }}  
{ "articleID" : "QQPX-R-3956-#aD8", "userID" : 2, "hidden": true, "postDate": "2017-01-02", "tags":["java", "elasticsearch"]} 
</code></pre>

``添加一个字段: view_cnt 浏览量``
<pre><code>
POST /es2_6/test1/_bulk
{ "update": { "_id": "1"} }
{ "doc" : {"view_cnt" : 30} }
{ "update": { "_id": "2"} }
{ "doc" : {"view_cnt" : 50} }
{ "update": { "_id": "3"} }
{ "doc" : {"view_cnt" : 100} }
{ "update": { "_id": "4"} }
{ "doc" : {"view_cnt" : 80} }
</code></pre>

``获取view_cnt 大于10 小于60 的文章``
<pre><code>
GET /es2_6/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        
        "range": {
          "view_cnt": {
            "gte": 10,
            "lte": 60
          }
        }
      }
    }
  }
}
</code></pre>


###日期的范围查询
``方式1:``
<pre><code>
GET /es2_6/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "postDate": {
            "gte" : "2018-05-10||-30d"
          }
        }
      }
    }
  }
}
</code></pre>

``方式2``
<pre><code>
GET /es2_6/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "postDate": {
            "gte": "now-60d",
            "lte": "now+1d"
          }
        }
      }
    }
  }
}
</code></pre>



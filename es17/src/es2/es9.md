###boost 权重的设置

####测试数据
``使用 es8 的数据``


````
需求:
1. 搜索标题中包含java的帖子
2. 同时，如果标题中包含hadoop或elasticsearch就优先搜索出来
3. 同时，如果一个帖子包含java hadoop，一个帖子包含java elasticsearch，包含hadoop的帖子要比elasticsearch优先搜索出来
````


<pre><code>
GET /es2_8/test1/_search
{
  "query": {
    "bool": {
      "must": [
         {"match": {
           "title": "java"
         }}
      ],
      "should": [
        {"match": {
          "title" : {
            "query": "hadoop",
            "boost" : 5
          }
        }},
        {"match": {
          "title": {
            "query": "elasticsearch",
            "boost" : 3
          }
        }}
      ]
    }
  }
}
</code></pre>
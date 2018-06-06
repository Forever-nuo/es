##基于bool 组和多条件的检索

###bool中可以包含的
````
must
must_not
should
filter
````

####准备数据
<pre><code>
POST /es2_4/test1/_bulk
{ "index": { "_id": 1 }}  
{ "articleID" : "XHDK-A-1293-#fJ3", "userID" : 1, "hidden": false, "postDate": "2017-01-01" }  
{ "index": { "_id": 2 }}  
{ "articleID" : "KDKE-B-9947-#kL5", "userID" : 1, "hidden": false, "postDate": "2017-01-02" }  
{ "index": { "_id": 3 }}  
{ "articleID" : "JODL-X-1937-#pV7", "userID" : 2, "hidden": false, "postDate": "2017-01-01" }  
{ "index": { "_id": 4 }}  
{ "articleID" : "QQPX-R-3956-#aD8", "userID" : 2, "hidden": true, "postDate": "2017-01-02" }  
</code></pre>

###示例1
````
检索:搜索发帖日期为2017-01-01，
或者帖子ID为XHDK-A-1293-#fJ3的帖子
同时要求帖子的发帖日期绝对不为2017-01-02
````

````
对应的sql语句
````
<pre><code>
select * from article 
where (postDate = "2017-01-01" or articleID ="XHDK-A-1293-#fJ3" ) and postDate ! = "2017-01-02"
</code></pre>

````
对应的es查询
````
<pre><code>
GET /es2_4/test1/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "bool": {
           "should" : [  {"term":{ "postDate" : "2017-01-01"     }},
                         {"term":{ "articleID.keyword":"KDKE-B-9947-#kL5"}}   
                      ],
            "must_not" :{"term":{"postDate":"2017-01-02"}}            
        }
      }
    }
  }
}
</code></pre>

###示例2
````
搜索帖子ID为XHDK-A-1293-#fJ3
或者是帖子ID为JODL-X-1937-#pV7而且发帖日期为2017-01-01的帖子
````

````
对应的sql语句'
select * from article
where articleID = XHDK-A-1293-#fJ3 
or (articleID = JODL-X-1937-#pV7 and postDate = 2017-01-01)
````
<pre><code>
GET   es2_4/test1/_search
{
	"query": {
		"constant_score": {
			"filter": {
				"bool": {
					"should": [{
							"term": {
								"articleID.keyword": "XHDK-A-1293-#fJ3"
							}
						},
						{
							"bool": {
								"must": [{
										"term": {
											"articleID": "JODL-X-1937-#pV7"
										}
									},
									{
										"term": {
											"postDate": "2017-01-01"
										}
									}
								]
							}
						}
					]
				}
			}
		}
	}
}
</code></pre>
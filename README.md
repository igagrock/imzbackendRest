
# API for an template website for articles



> ##Authentication Rules for API
> ###GET REQUESTS

```
1) users              -- Delete the resource after testing
2) users/{user_name}  -- AuthRequired
3) users/{user_name}/articles -- AuthRequiredGET
4) users/{user_name}/articles/{article_id} -- No AuthRequired
5) users/{user_name}/articles/{article_id}/comments -- No AuthRequired
6) users/{user_name}/articles/{article_id}/comments/{comment_id} -- NotIMPLEMENTED
7) users/{user_name}/comments -- AuthRequired
8) users/{user_name}/comments/{comment_id} --AuthRequired

```

  > ###POST REQUESTS
  
 ```
 1) users 						 -- AuthRequired
 2) users/{user_name}/articles	 -- AuthRequired
 3) users/{user_name}/articles/{article_id}/comments -- AuthRequiredGET
 ```
 
 
 > ###PUT REQUESTS
 
 
 ```
 1)  users/{user_name} -- AuthRequired
 2)  users/{user_name}/articles/{article_id} -- AuthRequired
 3)  users/{user_name}/comments/{comment_id} -- AuthRequired
 ```
 

> ###DELETE REQUESTS

```
 1)  users/{user_name} -- AuthRequired
 2)  users/{user_name}/articles/{article_id} -- AuthRequired
 3)  users/{user_name}/comments/{comment_id} -- AuthRequired
 ```





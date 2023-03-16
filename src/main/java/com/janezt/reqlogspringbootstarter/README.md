# req-log-spirngboot-starter


##引入依赖
```pom
        <dependency>
            <groupId>cn.chan</groupId>
            <artifactId>req-log-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```

##如果不想打印日志
```yml
component:
  req:
    logs:
      enabled: false
```
##请求实例
```log
18:13:48.096 [http-nio-8130-exec-2] INFO  c.j.r.c.i.LogInterceptor - [preHandle,46] - Start: /task/list
18:13:48.099 [http-nio-8130-exec-2] INFO  c.j.r.c.i.LogInterceptor - [preHandle,50] - invoke /task/list, Params: {"execution":["633"],"taskType":["5"]}, Headers: {"authorization":"Bearer 52a73216-721a-4d73-81cc-b088d4849f8f","content-length":"204","postman-token":"10db6bfb-efee-4802-bcb5-8e1c82c51f5a","host":"localhost:8130","content-type":"application/json","connection":"keep-alive","cache-control":"no-cache","accept-encoding":"gzip, deflate, br","user-agent":"PostmanRuntime/7.28.4","accept":"*/*"}
Creating a new SqlSession
Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2cdabd5a]
JDBC Connection [HikariProxyConnection@73324182 wrapping com.mysql.cj.jdbc.ConnectionImpl@31daf094] will be managed by Spring
==>  Preparing: select t.id, t.project, t.parent, t.execution, t.`module`, t.design, t.story, t.storyVersion, t.designVersion, t.fromBug, t.`name`, t.`type`, t.pri, t.estimate, t.consumed, t.`left`, t.deadline, t.`status`, t.subStatus, t.color,t.mailto, t.`desc`, t.version, t.openedBy, t.openedDate, t.assignedDate, t.estStarted, t.realStarted, t.finishedDate, t.finishedList, t.canceledBy, t.canceledDate, t.closedBy, t.closedDate, t.realDuration, t.planDuration, t.closedReason, t.lastEditedBy, t.lastEditedDate, t.activatedDate, t.deleted, t.preposition ,t.assignedTo , case when t.task_subject='1' then ( select formal_name from plm_material_info where id=t.execution ) when t.task_subject='2' then ( select name from plm_sample where id=t.execution ) when t.task_subject='3' then ( select project_name from plm_project_info where id=t.execution ) end executionName ,p.`project_no` executionNo ,t.finishedBy ,t.orderNum ,t.planEnd ,(select count(id) from plm_project_task where preposition = t.id) postPrepositionNum ,t.createBy ,t.createTime ,t.examinedate ,t.remarks ,t.task_type ,t.task_subject ,t.business_type ,p.project_principal PM ,p.project_no projectNo ,h.name_path productTypeNamePath from plm_project_task t left join plm_project_info p on p.id = t.execution left join plm_project_base pb on pb.id = p.project_id left join plm_product_hierarchy h on pb.product_type_id = h.id WHERE t.deleted = '0' and t.execution = ? and t.task_type = ? and case when t.task_subject='3' then t.id in ( select t.id from plm_project_task t left join plm_project_info p on p.id = t.execution left join plm_project_base pb on pb.id = p.project_id left join plm_product_hierarchy h on pb.product_type_id = h.id where 1=1 ) when t.task_subject='2' then t.id in ( select t.id from plm_project_task t left join plm_sample p on p.id = t.execution left join plm_bom_base b on p.bom_id=b.id left join plm_design_info i on b.design_id =i.id left join plm_product_hierarchy h on i.product_type_id = h.id where 1=1 ) when t.task_subject='1' then t.id in (select id from plm_project_task where task_subject='1' ) when t.task_subject='5' then t.id in (select id from plm_project_task where task_subject='5' ) end order by t.createTime desc
==> Parameters: 633(Long), 5(String)
<==    Columns: id, project, parent, execution, module, design, story, storyVersion, designVersion, fromBug, name, type, pri, estimate, consumed, left, deadline, status, subStatus, color, mailto, desc, version, openedBy, openedDate, assignedDate, estStarted, realStarted, finishedDate, finishedList, canceledBy, canceledDate, closedBy, closedDate, realDuration, planDuration, closedReason, lastEditedBy, lastEditedDate, activatedDate, deleted, preposition, assignedTo, executionName, executionNo, finishedBy, orderNum, planEnd, postPrepositionNum, createBy, createTime, examinedate, remarks, task_type, task_subject, business_type, PM, projectNo, productTypeNamePath
<==        Row: 71, 0, 0, 633, 0, 0, 0, 0, 0, 0, 项目风险任务, misc, 1, 0.0, 0.0, 0.0, 2021-11-16, wait, , , <<BLOB>>, <<BLOB>>, 0, admin, 2021-11-16 10:26:42, 2021-11-16 10:26:42, 0000-00-00, 0000-00-00 00:00:00, 0000-00-00 00:00:00, <<BLOB>>, , 0000-00-00 00:00:00, , 0000-00-00 00:00:00, 0, 0, , admin, 2021-11-16 13:47:36, 2021-11-16, 0, null, admin, 项目21SS0105001（款式列表测试_1115_003）, 21SS0105001, , 1.0, 2021-11-16, 0, admin, 2021-11-16 10:26:42, 2021-11-16, 11, 5, 3, 1, 舒灿, 21SS0105001, 蕉下11>伞类>晴雨伞
<==      Total: 1
Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2cdabd5a]
Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2cdabd5a] from current transaction
==>  Preparing: SELECT id,task_id,attachment_name,attachment_url FROM plm_task_attachment WHERE (task_id = ?)
==> Parameters: 71(Integer)
<==    Columns: id, task_id, attachment_name, attachment_url
<==        Row: 43, 71, PLM1.4.0任务分配.xlsx, /2021/11/16/6ba41861-6874-413f-ade5-80c41a2cadc7.xlsx
<==      Total: 1
Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2cdabd5a]
Transaction synchronization committing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2cdabd5a]
Transaction synchronization deregistering SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2cdabd5a]
Transaction synchronization closing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2cdabd5a]
18:13:48.365 [http-nio-8130-exec-2] INFO  c.j.r.c.i.LogInterceptor - [afterCompletion,86] - End /task/list, executeTime: 269 ms
```

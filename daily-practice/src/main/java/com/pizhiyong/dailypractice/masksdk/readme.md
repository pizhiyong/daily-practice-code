掩码sdk，针对某些场景，比如http接口、rpc接口执行掩码，只需要引入掩码sdk，
并且响应体中针对需要掩码的字段添加掩码注解即可.

在application.properties中配置掩码使用的数据源和开关
sensitive.mask.sql.session.datasource = dataSource
sensitive.mask.enable = true
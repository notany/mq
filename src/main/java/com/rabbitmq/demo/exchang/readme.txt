这里演示的是不同交换类型的区别，rabbitmq的交换分为：
1、direct(直连交换机，Routing Key==Binding Key，严格匹配)
2、fanout(广播交换机，把发送到该 Exchange 的消息路由到所有与它绑定的 Queue 中)
3、topic(主体交换机，Routing Key==Binding Key，支持模糊匹配)
4、headers(头交换机，根据发送的消息内容中的 x-match属性进行匹配)
5、default(默认交换机，类型还是direct，只不过rabbitmq做了默认处理)

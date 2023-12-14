# 简易版spring

## 功能
1. 会在启动的时候将所有controller下面的接口全部获取出来
2. 通过字典树构建成一个链表，在用户请求的时候可以通过handlerMapping来获取对应的方法
3. 然后通过handlerAdaptor来获取参数和执行对应的方法
4. 只需要和springboot一样声明方法接口，然后dispatchServlet里面可以将对应的参数传递到接口方法参数里面。
5. 统一配置所有的请求和响应编码

## todo
1. 添加ioc 




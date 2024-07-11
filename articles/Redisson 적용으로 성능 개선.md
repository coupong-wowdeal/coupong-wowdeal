Lettuce로 구현했던 Spin lock은 루프를 통해 지속적으로 Redis에 부하를 가하는 문제가 있다.

이는 lock해제에 대해 즉각적인 전파를 받지 않고, 불필요한 오버헤드가 발생하는 단점이 있다.

이것을 redisson을 이용해 Pub/Sub 구조로 바꿔주면, 

지속적으로 차지하는 리소스가 가벼워지고 lock에 대한 이벤트가 즉각적으로 전파되어 오버헤드가 작아져 빠른 처리를 가능하게 할 수 있다.

 
Lettuce

![image](https://github.com/coupong-wowdeal/coupong-wowdeal/assets/11582792/bebc123c-b9a8-45bf-b37f-a72a6f1870d8)


Redisson 

![image](https://github.com/coupong-wowdeal/coupong-wowdeal/assets/11582792/af89e90f-c99a-4bf3-b63e-0577fe3d3d88)

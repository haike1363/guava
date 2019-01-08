package pers.haike.demo;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

public class GuavaEventBus {

    public static class TestEvent {
        private final int message;

        public TestEvent(int message) {
            this.message = message;
        }

        public int getMessage() {
            return message;
        }
    }


    @Test
    public void multiListener() {
        EventBus eventBus = new EventBus("test");
        eventBus.register(new Object() {
            @Subscribe
            public void listenInteger(Integer event) {
                System.out.println("1 " +event.toString());
            }

            @Subscribe
            public void listenLong(Long event) {
                System.out.println("1 " +event.toString());
            }
        });
        eventBus.register(new Object() {
            @Subscribe
            public void listenInteger(Integer event) {
                System.out.println("2 " +event.toString());
            }

            @Subscribe
            public void listenLong(Long event) {
                System.out.println("2 " +event.toString());
            }
        });
        eventBus.post(new Integer(100));
        eventBus.post(new Long(800882934));
    }


    @Test
    public void testDeadEvent() throws Exception {
        // 如果EventBus发送的消息都不是订阅者关心的称之为Dead Event
        EventBus eventBus = new EventBus("test");
        eventBus.register(new Object() {
            @Subscribe
            public void listen(DeadEvent event) {
                System.out.println(event.toString());
            }
        });

        eventBus.post(new TestEvent(200));
        eventBus.post(new TestEvent(300));
    }


    public class NumberListener {
        private Number lastMessage;

        @Subscribe
        public void listen(Number integer) {
            lastMessage = integer;
            System.out.println("Message Number:" + lastMessage);
        }

        public Number getLastMessage() {
            return lastMessage;
        }
    }


    @Test
    public void testEventInherit() throws Exception {
        // 消息继承
        // 如果Listener A监听Event A, 而Event A有一个子类Event B, 此时Listener A将同时接收Event A和B消息
        EventBus eventBus = new EventBus("test");
        eventBus.register(new Object() {
            @Subscribe
            public void listen(Integer integer) {
                System.out.println("currentThread:" + Thread.currentThread().getId());
                System.out.println("Message Integer:" + integer);
            }
        });
        eventBus.register(new Object() {
            @Subscribe
            public void listen(Number integer) {
                System.out.println("currentThread:" + Thread.currentThread().getId());
                System.out.println("Message Number:" + integer);
            }
        });
        System.out.println("currentThread:" + Thread.currentThread().getId());
        eventBus.post(new Integer(100));
        eventBus.post(new Long(200L));
    }
}

package poc.domain.common.publish;

import poc.domain.common.event.BaseEvent;

public interface EventPublisher {

    void publish(BaseEvent event);
}

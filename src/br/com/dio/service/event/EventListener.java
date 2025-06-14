package br.com.dio.service.event;

import br.com.dio.service.event.enuns.EventEnum;

public interface EventListener {

    void update(final EventEnum eventType);

}

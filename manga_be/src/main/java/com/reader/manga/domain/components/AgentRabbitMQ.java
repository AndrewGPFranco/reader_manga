package com.reader.manga.domain.components;

import com.reader.manga.configs.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgentRabbitMQ {

    private final RabbitTemplate rabbitTemplate;

    private final CopyOnWriteArrayList<SseEmitter> emittersNotificacaoNovoCapitulo = new CopyOnWriteArrayList<>();

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receberMensagem(String mensagem) {
        enviaNotificacaoNovoCapitulo(mensagem);
    }

    public void enviarMensagem(String mensagem) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, mensagem);
    }

    public void addEmitter(SseEmitter emitter) {
        emittersNotificacaoNovoCapitulo.add(emitter);

        Runnable remover = () -> emittersNotificacaoNovoCapitulo.remove(emitter);

        emitter.onCompletion(remover);
        emitter.onTimeout(remover);
        emitter.onError(t -> remover.run());
    }

    private void enviaNotificacaoNovoCapitulo(String mensagem) {
        for (SseEmitter emitter : emittersNotificacaoNovoCapitulo) {
            try {
                emitter.send(mensagem);
            } catch (IOException e) {
                emitter.complete();
                emittersNotificacaoNovoCapitulo.remove(emitter);
            }
        }
    }

}

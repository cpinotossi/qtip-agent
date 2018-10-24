package com.akamai.qtip;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import com.akamai.qtip.Messages.AgentDescriptor;
import com.google.protobuf.Any;
import com.google.protobuf.Message;

public abstract class MessageHandler<TMessage extends Message> {
   
	private Class<TMessage> messageClass;
	
    public MessageHandler() {
    	this.messageClass = getMessageClass();
    }

	@SuppressWarnings("unchecked")
	private Class<TMessage> getMessageClass() {
        ParameterizedType superclass =
            (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<TMessage>) superclass.getActualTypeArguments()[0];
    }
	
	@SuppressWarnings("unchecked")
	public String getTypeUrl() {
		TMessage msg;
		try {
			msg = (TMessage)messageClass.getMethod("getDefaultInstance").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
			return "";
		}
		return Any.pack(msg).getTypeUrl();
	}
	
	protected abstract void handle(AgentDescriptor sender, TMessage message) throws Exception;

	public void handle(AgentDescriptor sender, Any message) throws Exception {
		TMessage typedMessage = message.unpack(this.messageClass);
		handle(sender, typedMessage);
	}
}

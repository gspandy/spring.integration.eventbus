package org.springframework.integration.test.eventbus;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.foobar.application.FooBarApplicationService;
import com.foobar.domain.eventstore.EventStore;
import com.foobar.domain.model.DomainEvent;
import com.foobar.domain.model.DomainEventPublisher;


@RunWith(JUnit4.class)
@PrepareForTest(DomainEventPublisher.class)
public class SpringEventbusTestcase{

	@Test
	public void springEventBusVerboseSubscribeTest() {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/spring-eventbus-verbose-subscribe-ctxt.xml");	
		testEventBus(context);
	
	}
	
	@Test
	public void springEventBusNonVerboseSubscribeTest() {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/spring-eventbus-non-verbose-subscribe-ctxt.xml");
		testEventBus(context);
	
	}

	
	private void testEventBus(ApplicationContext context){				
		
		FooBarApplicationService applicationService = context.getBean(FooBarApplicationService.class);		
		applicationService.updateFoo();
		applicationService.updateBar();
		
		// Test sending arbitrary domain to validate that it gets consumed by BaseFooBarApplicationService
		DomainEventPublisher.publish(new DomainEvent(){});
		
		EventStore eventStore = context.getBean(EventStore.class);		
		
		//Verify all above domain events are logged in event store
		Assert.assertTrue(eventStore.allDomainEventsSince(-1l).size()==3);
	}
	
}
package io.switchbit;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;
import org.apache.camel.util.ObjectHelper;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.binding.BindingTargetFactory;

@UriEndpoint(scheme = "scst", title = "Spring Cloud Stream", syntax = "scst:destination", label = "spring,cloud,stream")
public class SpringCloudStreamEndpoint extends DefaultEndpoint {

	@UriPath(description = "The target destination of a channel on the bound middleware")
	@Metadata(required = "true")
	private String destination;

	private final SpringCloudStreamEndpointConfiguration configuration;
	private final ConfigurableListableBeanFactory beanFactory;
	private final CamelBindingService bindingService;
	private final BindingTargetFactory bindingTargetFactory;

	public SpringCloudStreamEndpoint(String uri, String destination,
			SpringCloudStreamComponent component,
			SpringCloudStreamEndpointConfiguration configuration,
			ConfigurableListableBeanFactory beanFactory,
			CamelBindingService bindingService,
			BindingTargetFactory bindingTargetFactory) {
		super(uri, component);

		this.destination = ObjectHelper.notNull(destination, "destination");
		this.configuration = ObjectHelper.notNull(configuration, "configuration");
		this.beanFactory = beanFactory;
		this.bindingService = bindingService;
		this.bindingTargetFactory = bindingTargetFactory;
	}

	@Override
	public Producer createProducer() throws Exception {
		bindingService.getBindingServiceProperties().setCamelConfiguration(configuration,
				getDestination());
		return new SpringCloudStreamProducer(this);
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		bindingService.getBindingServiceProperties().setCamelConfiguration(configuration,
				getDestination());
		return new SpringCloudStreamConsumer(this, processor);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getDestination() {
		return destination;
	}

	public SpringCloudStreamEndpointConfiguration getConfiguration() {
		return configuration;
	}

	public ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

	public BindingService getBindingService() {
		return bindingService;
	}

	public BindingTargetFactory getBindingTargetFactory() {
		return bindingTargetFactory;
	}
}

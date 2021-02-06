package com.github.jim2ha0.config;


@FunctionalInterface
public interface SchedulingConfigurer {

	void configureTasks(NamedScheduledTaskRegistrar taskRegistrar);

}

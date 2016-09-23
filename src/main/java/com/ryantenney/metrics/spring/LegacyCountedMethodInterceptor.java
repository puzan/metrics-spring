/**
 * Copyright (C) 2012 Ryan W Tenney (ryan@10e.us)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ryantenney.metrics.spring;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.annotation.Counted;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.util.ReflectionUtils.MethodFilter;

import static com.ryantenney.metrics.spring.AnnotationFilter.PROXYABLE_METHODS;

@Deprecated
class LegacyCountedMethodInterceptor extends AbstractMetricMethodInterceptor<Counted, Counter> {

	public static final Class<Counted> ANNOTATION = Counted.class;
	public static final Pointcut POINTCUT = new AnnotationMatchingPointcut(null, ANNOTATION);
	public static final MethodFilter METHOD_FILTER = new AnnotationFilter(ANNOTATION, PROXYABLE_METHODS);

	public LegacyCountedMethodInterceptor(final MetricRegistry metricRegistry,
			final Class<?> targetClass,
			NamingStrategy namingStrategy) {
		super(metricRegistry, targetClass, ANNOTATION, METHOD_FILTER, namingStrategy);
	}

	@Override
	protected Object invoke(MethodInvocation invocation, Counter counter, Counted annotation) throws Throwable {
		try {
			counter.inc();
			return invocation.proceed();
		}
		finally {
			if (!annotation.monotonic()) {
				counter.dec();
			}
		}
	}

	@Override
	protected Counter buildMetric(MetricRegistry metricRegistry, String metricName, Counted annotation) {
		return metricRegistry.counter(metricName);
	}

	static AdviceFactory adviceFactory(final MetricRegistry metricRegistry, final NamingStrategy namingStrategy) {
		return new AdviceFactory() {
			@Override
			public Advice getAdvice(Object bean, Class<?> targetClass) {
				return new LegacyCountedMethodInterceptor(metricRegistry, targetClass, namingStrategy);
			}
		};
	}

}

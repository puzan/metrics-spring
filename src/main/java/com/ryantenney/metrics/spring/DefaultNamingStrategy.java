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

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;

import com.codahale.metrics.annotation.*;

import static com.codahale.metrics.MetricRegistry.name;

public class DefaultNamingStrategy implements NamingStrategy {
	private String chooseName(String explicitName,
			boolean absolute,
			Class<?> klass,
			Member member,
			String... suffixes) {
		if (explicitName != null && !explicitName.isEmpty()) {
			if (absolute) {
				return explicitName;
			}
			return name(klass.getCanonicalName(), explicitName);
		}
		return name(name(klass.getCanonicalName(), member.getName()), suffixes);
	}

	@Override
	public String getName(Class<?> klass, Member member, Annotation annotation) {
		AnnotationInfo annotationInfo = getAnnotationInfo(annotation);
		return chooseName(annotationInfo.getName(), annotationInfo.isAbsolute(), klass, member);
	}

	protected AnnotationInfo getAnnotationInfo(Annotation annotation) {
		if (annotation instanceof Timed) {
			Timed metricAnnotation = (Timed) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof Metered) {
			Metered metricAnnotation = (Metered) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof Gauge) {
			Gauge metricAnnotation = (Gauge) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof CachedGauge) {
			CachedGauge metricAnnotation = (CachedGauge) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof ExceptionMetered) {
			ExceptionMetered metricAnnotation = (ExceptionMetered) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof Counted) {
			Counted metricAnnotation = (Counted) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof Metric) {
			Metric metricAnnotation = (Metric) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof com.ryantenney.metrics.annotation.CachedGauge) {
			com.ryantenney.metrics.annotation.CachedGauge metricAnnotation =
					(com.ryantenney.metrics.annotation.CachedGauge) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof com.ryantenney.metrics.annotation.Counted) {
			com.ryantenney.metrics.annotation.Counted metricAnnotation = (com.ryantenney.metrics.annotation.Counted) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}
		if (annotation instanceof com.ryantenney.metrics.annotation.Metric) {
			com.ryantenney.metrics.annotation.Metric metricAnnotation = (com.ryantenney.metrics.annotation.Metric) annotation;
			return new AnnotationInfo(metricAnnotation.name(), metricAnnotation.absolute());
		}

		return new AnnotationInfo("", false);
	}

	protected static class AnnotationInfo {
		private final String name;
		private final boolean absolute;

		AnnotationInfo(String name, boolean absolute) {
			this.name = name;
			this.absolute = absolute;
		}

		public String getName() {
			return name;
		}

		public boolean isAbsolute() {
			return absolute;
		}
	}
}

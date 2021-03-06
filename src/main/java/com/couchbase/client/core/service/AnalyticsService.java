/*
 * Copyright (c) 2017 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.couchbase.client.core.service;

import com.couchbase.client.core.CoreContext;
import com.couchbase.client.core.ResponseEvent;
import com.couchbase.client.core.endpoint.Endpoint;
import com.couchbase.client.core.endpoint.analytics.AnalyticsEndpoint;
import com.couchbase.client.core.env.CoreEnvironment;
import com.couchbase.client.core.service.strategies.RoundRobinSelectionStrategy;
import com.couchbase.client.core.service.strategies.SelectionStrategy;
import com.lmax.disruptor.RingBuffer;

/**
 * The {@link AnalyticsService} is composed of and manages {@link AnalyticsEndpoint}s.
 *
 * @author Michael Nitschinger
 * @since 1.4.3
 */
public class AnalyticsService extends PooledService {

    /**
     * The endpoint selection strategy.
     */
    private static final SelectionStrategy STRATEGY = new RoundRobinSelectionStrategy();

    /**
     * The endpoint factory.
     */
    private static final EndpointFactory FACTORY = new AnalyticsEndpointFactory();

    /**
     * Creates a new {@link AnalyticsService}.
     *
     * @param hostname the hostname of the service.
     * @param bucket the name of the bucket.
     * @param password the password of the bucket.
     * @param port the port of the service.
     * @param ctx the shared context.

     */
    @Deprecated
    public AnalyticsService(final String hostname, final String bucket, final String password, final int port,
                            final CoreContext ctx) {
        this(hostname, bucket, bucket, password, port, ctx);
    }

    /**
     * Creates a new {@link AnalyticsService}.
     *
     * @param hostname the hostname of the service.
     * @param bucket the name of the bucket.
     * @param username the user authorized for bucket access.
     * @param password the password of the user.
     * @param port the port of the service.
     * @param ctx the shared context.
     */
    public AnalyticsService(final String hostname, final String bucket, final String username, final String password, final int port,
        final CoreContext ctx) {
        super(hostname, bucket, username, password, port, ctx, ctx.environment().analyticsServiceConfig(), FACTORY, STRATEGY);
    }

    @Override
    public ServiceType type() {
        return ServiceType.ANALYTICS;
    }

    /**
     * The factory for {@link AnalyticsEndpoint}s.
     */
    static class AnalyticsEndpointFactory implements EndpointFactory {
        public Endpoint create(final String hostname, final String bucket, final String username,
            final String password, final int port, final CoreContext ctx) {
            return new AnalyticsEndpoint(hostname, bucket, username, password, port, ctx);
        }
    }
}

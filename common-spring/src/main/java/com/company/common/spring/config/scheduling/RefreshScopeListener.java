package com.company.common.spring.config.scheduling;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({RefreshScopeRefreshedEvent.class})
public class RefreshScopeListener implements ApplicationListener<RefreshScopeRefreshedEvent> {
    private final List<RefreshScheduler> refreshSchedulers;

    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        this.refreshSchedulers.forEach(RefreshScheduler::materializeAfterRefresh);
    }

    public RefreshScopeListener(final List<RefreshScheduler> refreshSchedulers) {
        this.refreshSchedulers = refreshSchedulers;
    }
}

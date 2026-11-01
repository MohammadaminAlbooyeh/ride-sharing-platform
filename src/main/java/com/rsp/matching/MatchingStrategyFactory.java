package com.rsp.matching;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resolves matching strategies by their {@link MatchingStrategy#name()}.
 */
@Component
public class MatchingStrategyFactory {

    private final Map<String, MatchingStrategy> strategies;
    private final MatchingStrategy defaultStrategy;

    public MatchingStrategyFactory(List<MatchingStrategy> strategyBeans) {
        this.strategies = strategyBeans.stream()
                .collect(Collectors.toMap(MatchingStrategy::name, Function.identity()));
        this.defaultStrategy = strategies.get(NearestDriverStrategy.NAME);
    }

    public Optional<MatchingStrategy> get(String name) {
        return Optional.ofNullable(strategies.get(name));
    }

    public MatchingStrategy defaultStrategy() {
        return defaultStrategy;
    }

    public List<String> availableNames() {
        return strategies.keySet().stream().sorted().toList();
    }
}

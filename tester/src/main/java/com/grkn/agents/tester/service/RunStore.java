package com.grkn.agents.tester.service;

import com.grkn.agents.tester.model.RunRecord;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RunStore {

    private final Map<String, RunRecord> runs = new ConcurrentHashMap<>();

    public RunRecord save(RunRecord record) {
        runs.put(record.runId(), record);
        return record;
    }

    public Optional<RunRecord> findById(String runId) {
        return Optional.ofNullable(runs.get(runId));
    }
}

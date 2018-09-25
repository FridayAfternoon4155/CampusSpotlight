package com.fridayafternoon.campusspotlight;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

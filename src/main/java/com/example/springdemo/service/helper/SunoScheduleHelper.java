package com.example.springdemo.service.helper;

import com.example.springdemo.bean.dto.SunoClipDTO;

import java.util.*;
import java.util.concurrent.*;

public class SunoScheduleHelper {

    public static SunoScheduleHelper instance() {
        return InnerClass.instance;
    }

    public interface TimerScheduler {
        void onSchedule(Timer timer, CountDownLatch countDownLatch, SunoClipDTO clip);
    }

    private final ExecutorService executorService =
            new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(8));

    public boolean scheduleEach(List<SunoClipDTO> generationList, TimerScheduler timerScheduler,
                                long delayInMillion, long periodInMillion, long timeoutInSecond
    ) {
        CountDownLatch countDownLatch = new CountDownLatch(generationList.size());
        CopyOnWriteArrayList<Timer> timerList = new CopyOnWriteArrayList<>();
        for (SunoClipDTO clip: generationList) {
            executorService.submit(() -> {
                Timer timer = new Timer();
                timerList.add(timer);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timerScheduler.onSchedule(timer, countDownLatch, clip);
                    }
                }, delayInMillion, periodInMillion);
            });
        }
        boolean success = false;
        try {
            success = countDownLatch.await(timeoutInSecond, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Timer timer: timerList) {
            timer.cancel();
        }
        return success;
    }

    private static class InnerClass {

        public static SunoScheduleHelper instance = new SunoScheduleHelper();

    }

}

package easybatch;

import java.io.File;
import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.writer.StandardOutputRecordWriter;
import org.easybatch.flatfile.DelimitedRecordMapper;
import org.easybatch.flatfile.FlatFileRecordReader;

import common.Tweet;

/**
 * Easy Batch launcher.
 */
public class EasyBatchHelloWorldLauncher {

    public static void main(String[] args) throws Exception {

        File tweets = new File(EasyBatchHelloWorldLauncher.class.getResource("/tweets.csv").toURI());

        // Build a job
        Job job = new JobBuilder()
                .reader(new FlatFileRecordReader(tweets))
                .filter(new HeaderRecordFilter())
                .mapper(new DelimitedRecordMapper<>(Tweet.class, "id", "user", "message"))
                .processor(new TweetProcessor())
                .writer(new StandardOutputRecordWriter())
                .build();

        // Run the job
        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();

    }

}

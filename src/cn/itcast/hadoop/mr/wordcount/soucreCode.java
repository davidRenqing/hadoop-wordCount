package cn.itcast.hadoop.mr.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.v2.TestMRJobsWithHistoryService;
import org.apache.hadoop.yarn.client.api.YarnClient;

public class soucreCode {
	private YarnClient client;
	public int a;
	 private  void  str()
	{
		this.client=YarnClient.createYarnClient();
		client.init(new Configuration());
		
		
	}
}

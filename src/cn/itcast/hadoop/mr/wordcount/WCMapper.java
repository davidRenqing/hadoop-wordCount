package cn.itcast.hadoop.mr.wordcount;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


//4个泛型中，前两个制定mapper输入数据的类型，KEYIN 是输入的key 的类型，VALUEIN 是输入的 value 的类型。
//KEYOUT VALUEOUT 是map函数的输出的类型
//map函数进来的形参 KEYIN VALUEIN，我们是不能控制的，那么我们根据框架一致就好了，框架传给我们什么我们就用什么泛型就好了。
//默认情况下，框架传递给我们的mapper的输入数据中，key是要处理的文本中一行的起始偏移量。这一行的内容作为 value
//KEYIN : long ;VALUEIN : string
//输出：String ；输出的内容是：Long 类型的数据
//java中使用网络传输，要将数据进行序列化；hadoop不使用java的序列化的方法；hadoop有自己的序列化的接口，可以少很多附加的信息


public class WCMapper extends Mapper<LongWritable, Text, Text, LongWritable>{

	//Mapper 类要重新 map这个方法，每一行的数据调用一次 map这个方法
		//mapreduce 框架每读一行数据就调用一次该
	private static String hexString="0123456789ABCDEF"; 
	
	//写这个程序用户编码格式的转变
		public static Text tranformText(Text text1,String encoding)
		{
			String value1=null;
			try {
				value1=new String(text1.getBytes(),0,text1.getLength(),encoding);
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return new Text(value1);
		}
		
		//新填入的方法，将16进制转换成字符串
		public static String decode(String bytes) 
		{ 
		ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2); 
		//将每2位16进制整数组装成一个字节 
		for(int i=0;i<bytes.length();i+=2) 
		{
		baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1)))); 
		System.out.println(new String(baos.toByteArray()));
		}
		return new String(baos.toByteArray()); 
		} 
		
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
				throws IOException, InterruptedException {
			//具体业务逻辑就写在这个方法体中，而且我们业务要处理的数据已经被框架传递进来，在方法的参数中 key-value 
			// key 是一行数据的起始偏移量。value是这一行的文本内容
			
//			Text outcome=tranformText(value, "GB2314");
//			String line=outcome.toString();
//			String line=decode(value.toString());
		
			//1.首先是切分单词
//			String line = value.toString();             //将传进来的文件的一行，value转换成 string类型的数据
			
			System.out.println(value.toString());
			String[] words=StringUtils.split(value.toString(), " ");              //使用这个方法 line 类型按照 空格 进行切分.将切分完之后的单词，放到 words 这个String 数组当中
			
			//遍历这个单词数组，输出为 kv形式 k:单词 v:1
			for(String word:words)
			{
				// 使用这个输出的工具 context 将word 输出出去
				System.out.println(word);
				context.write(new Text(word), new LongWritable(1));         //将这一行输出过程 
			}
		} 
}

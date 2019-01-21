package Test;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class SolrJ {

	public void addDocument() throws Exception{
		//创建连接(单机版HttpSolrServer 集群版CloudSolrServer) 
		SolrServer solrServer = new HttpSolrServer("http://192.168.124.128:8080/solr");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//操作文档
		//document.addField(name, value);
		//把文档那个对象写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
}

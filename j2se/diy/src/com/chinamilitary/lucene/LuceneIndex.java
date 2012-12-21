package com.chinamilitary.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;

import com.chinamilitary.bean.Article;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.util.Constants;

public class LuceneIndex {
	
	private static Log log = LogFactory.getLog(LuceneIndex.class);

	private String indexPath;
	
	private static LuceneIndex instance = null;
	
	private static IndexWriter writer = null;
	
	private final StandardAnalyzer analyzer = new StandardAnalyzer();
	
	private LuceneIndex(String indexPath){
		this.indexPath = indexPath;
		writer = createIndexWriter(indexPath);
	}
	
	private IndexWriter createIndexWriter(String indexPath){
		File file = null;
		try {
			file = new File(indexPath);
			if(!file.exists()){
				log.debug(">> create index file");
				file.getParentFile().mkdirs();
			}
			if(file.isDirectory()){
				File[] s = file.listFiles();
				for(File s2:s){
					if(s2.delete()){
						log.debug(">> delete index file");
					}
				}
			}
			
			writer = new IndexWriter(file.getAbsolutePath(), analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		} catch (CorruptIndexException e) {
			log.error(">> createIndexWriter CorruptIndexException:"+e);
			return null;
		} catch (LockObtainFailedException e) {
			log.error(">> createIndexWriter LockObtainFailedException:"+e);
			return null;
		} catch (IOException e) {
			log.error(">> createIndexWriter IOException:"+e);
			return null;
		}
		return writer;
	}
	
	public static LuceneIndex getInstance(){
		if(instance == null)
			instance = new LuceneIndex(Constants.INDEX_DIR);
		return instance;
	}
	
	public boolean addIndex(Article article){
		
		if(null == writer)
			writer = createIndexWriter(indexPath);
		
		Document doc = new Document();
		Field field = new Field("id",String.valueOf(article.getId()),Field.Store.YES,Field.Index.TOKENIZED);
		doc.add(field);
		
		field = new Field("title",article.getTitle(),Field.Store.YES,Field.Index.TOKENIZED);
		doc.add(field);
		
		field = new Field("uri",article.getArticleUrl(),Field.Store.YES,Field.Index.TOKENIZED);
		doc.add(field);
		
		field = new Field("webid",String.valueOf(article.getWebId()),Field.Store.YES,Field.Index.TOKENIZED);
		doc.add(field);
		
		field = new Field("status",article.getText(),Field.Store.YES,Field.Index.TOKENIZED);
		doc.add(field);
		
		field = new Field("cdate",article.getCreateTime().toLocaleString(),Field.Store.YES,Field.Index.TOKENIZED);
		doc.add(field);
		
		try {
			writer.addDocument(doc);
			log.debug(">> add Article["+article.getTitle()+"|"+article.getArticleUrl()+"] to Index");
		} catch (CorruptIndexException e) {
			log.error(">> createIndexWriter CorruptIndexException:"+e);
			return false;
		} catch (IOException e) {
			log.error(">> createIndexWriter IOException:"+e);
			return false;
		}		return true;
	}
	
	public static void closeWriter(){
		if(null != writer){
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				log.error(">> close writer CorruptIndexException:"+e);
				e.printStackTrace();
			} catch (IOException e) {
				log.error(">> close writer IOException:"+e);
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();
		int pageSize = 500;
		int pageNum = 1;
		LuceneIndex lucene = LuceneIndex.getInstance();
		try{
			int c = articleDao.getCount();
			System.out.println(" > c:"+c);
			pageNum = c/pageSize+(c%pageSize == 0 ? 0:1);
			System.out.println( " > pageNum:" + pageNum);
			for(int i=0;i<pageNum;i++){
				List<Article> list = articleDao.findByPage(true, pageSize, pageNum*i);
				for(Article art:list){
					lucene.addIndex(art);
				}
				Thread.sleep(200);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}

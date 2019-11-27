package cn.itcast.store.domain;


import java.util.List;

/**
 * 存放分页相关的数据
 *
 */
public class PageModel {
	//基本属性
	private int currentPageNum;//当前页数，由用户指定				*
	private int pageSize = 5 ;//每页显示的条数，固定的				*
	private int totalRecords;//总记录条数，数据库查出来的			    *
	private int totalPageNum;//总页数，计算出来的					*
	private int startIndex;//每页开始记录的索引，计算出来的			    *
	private int prePageNum;//上一页							    *
	private int nextPageNum;//下一页							    *
	
	private List list;//已经分好页的结果集,该list中只有10条记录
	
	
	
	//扩展属性
	//一共每页显示9个页码按钮
	private int startPage;//开始页码
	private int endPage;//结束页码
	
	//完善属性
	private String url;
	
	


	//要想使用我的分页，必须给我两个参数。一个是要看哪一页，另一个是总记录条数
	public PageModel(int currentPageNum,int totalRecords,int pageSize){
		this.currentPageNum = currentPageNum;
		this.totalRecords = totalRecords;
		this.pageSize=pageSize;
		
		//计算查询记录的开始索引
		startIndex = (currentPageNum-1)*pageSize;
		//计算总页数
		totalPageNum = totalRecords%pageSize==0?(totalRecords/pageSize):(totalRecords/pageSize+1);
		
		
		startPage = currentPageNum - 4; //5
		endPage = currentPageNum + 4;  //13
		//看看总页数够不够9页
		if(totalPageNum>9){
			//超过了9页
			if(startPage < 1){
				startPage = 1;
				endPage = startPage+8;
			}
			if(endPage>totalPageNum){
				endPage = totalPageNum;
				startPage = endPage-8;
			}
		}else{
			//不够9页
			startPage = 1;
			endPage = totalPageNum;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getPrePageNum() {
		prePageNum = currentPageNum-1;
		if(prePageNum<1){
			prePageNum = 1;
		}
		return prePageNum;
	}

	public int getNextPageNum() {
		nextPageNum = currentPageNum+1;
		if(nextPageNum>totalPageNum){
			nextPageNum = totalPageNum;
		}
		return nextPageNum;
	}

	
	
	
	public int getCurrentPageNum() {
		return currentPageNum;
	}


	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getTotalRecords() {
		return totalRecords;
	}


	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}


	public int getTotalPageNum() {
		return totalPageNum;
	}


	public void setTotalPageNum(int totalPageNum) {
		this.totalPageNum = totalPageNum;
	}


	public int getStartIndex() {
		return startIndex;
	}


	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}


	


	public void setPrePageNum(int prePageNum) {
		this.prePageNum = prePageNum;
	}


	

	public void setNextPageNum(int nextPageNum) {
		this.nextPageNum = nextPageNum;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}


	
}

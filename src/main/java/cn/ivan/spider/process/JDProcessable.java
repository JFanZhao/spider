package cn.ivan.spider.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.ivan.spider.domain.Page;
import cn.ivan.spider.utils.HtmlUtils;
import cn.ivan.spider.utils.PageUtils;

public class JDProcessable implements Processable {

	public void process(Page page) {
		// TODO Auto-generated method stub
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		// 相当于htmlcleaner对页面进行处理
		TagNode rootNode = htmlCleaner.clean(page.getContent());
		if(page.getUrl().startsWith("http://item.jd.com")){//表示是商品详情页
			processProduct(page, rootNode);
		}else{//处理页面的url
			String next_url = HtmlUtils.getAttributeByAttr(rootNode, "//*[@id=\"J_topPage\"]/a[2]", "href");
			if(!next_url.equals("javascript:;")){
				System.out.println("http://list.jd.com"+next_url.replace("&amp;", "&"));
				String x = "http://list.jd.com"+next_url.replace("&amp;", "&");
				page.addUrl(x);
			}
			try {
				Object[] evaluateXPath = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
				for (Object object : evaluateXPath) {
					TagNode tagNode = (TagNode)object;
					String goodsUrl = tagNode.getAttributeByName("href");
					page.addUrl(goodsUrl);
				}
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 解析商品详细信息
	 * @param page
	 * @param rootNode
	 */
	private void processProduct(Page page, TagNode rootNode) {
		try {
			// 标题
			page.addField("title", HtmlUtils.getText(rootNode, "//*[@id=\"name\"]/h1"));
			// 获取图片地址
			page.addField("pic_url", "http:" +HtmlUtils.getAttributeByAttr(rootNode, "//*[@id=\"spec-n1\"]/img", "src"));
			// 价格：注意，价格是使用ajax异步请求动态写到页面的。所以不能使用普通xpath方式获取
			/*
			 * evaluateXPath = rootNode.evaluateXPath("//*[@id=\"jd-price\"]");
			 * if(evaluateXPath!=null && evaluateXPath.length>0){ TagNode
			 * priceNode = (TagNode)evaluateXPath[0];
			 * System.out.println("--"+priceNode.getText().toString()+"--"); }
			 */
			// 活得商品编号
			String url = page.getUrl();
			Pattern compile = Pattern.compile("http://item.jd.com/([0-9]+).html");
			Matcher matcher = compile.matcher(url);
			String goodId = null;
			if (matcher.find()) {
				goodId = matcher.group(1);
				page.setGoodId("JD_"+goodId);
			}

			// 获取价格
			String price_object = PageUtils.getContent("http://p.3.cn/prices/get?skuid=J_" + goodId);
			// 把价格解析出来，使用jsonarray，还有url中的商品编号不能写死，需要动态获取。
			JSONArray jsonArray = new JSONArray(price_object);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			float price = Float.parseFloat(jsonObject.getString("p"));
			page.addField("price", ""+price);
			//规格参数
			Object[] evaluateXPath = rootNode.evaluateXPath("//*[@id=\"product-detail-2\"]/table/tbody/tr");
			JSONArray jsonArray2 = new JSONArray();
			for (Object object : evaluateXPath) {
				TagNode trNode = (TagNode)object;
				if(!trNode.getText().toString().trim().equals("")){//把tr为空的标签过滤掉
					JSONObject jsonObject2 = new JSONObject();
					Object[] evaluateXPath2 = trNode.evaluateXPath("//th");
					if(evaluateXPath2!=null && evaluateXPath2.length>0){
						//tr下面是th标签
						TagNode thNode = (TagNode)evaluateXPath2[0];
						jsonObject2.put("name", "");
						jsonObject2.put("value", thNode.getText().toString());
					}else{
						evaluateXPath2 = trNode.evaluateXPath("//td");
						//tr下面是td标签
						TagNode tdNode1 = (TagNode)evaluateXPath2[0];
						TagNode tdNode2 = (TagNode)evaluateXPath2[1];
						jsonObject2.put("name", tdNode1.getText().toString());
						jsonObject2.put("value", tdNode2.getText().toString());
					}
					jsonArray2.put(jsonObject2);
				}
			}
			page.addField("spec", jsonArray2.toString());
			/*
			 * evaluateXPath = rootNode.evaluateXPath(
			 * "//*[@id=\"product-detail-2\"]/table/tbody/tr");
			 * if(evaluateXPath!=null&&evaluateXPath.length>0){ for (int i = 0;
			 * i < evaluateXPath.length; i++) { TagNode trNode =
			 * (TagNode)evaluateXPath[i]; TagNode[] allElements =
			 * trNode.getAllElements(true); for (TagNode tagNode : allElements)
			 * { System.out.print(tagNode.getText().toString()+":"); }
			 * System.out.println(); }
			 * 
			 * System.out.println(); }
			 */
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package com.taotao.portal.service.Impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;

/**
 * 
 * @ClassName ItemServiceImpl
 * @Description 商品信息管理 
 * @Author xll
 * @Date 2019年1月6日 下午3:28:30
 * @Version 1.0
 *
 */
@Service
public class ItemServiceImpl implements ItemService{

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	
	@Value("${ITEM_DESC_URL}")
	private String ITEM_DESC_URL;
	
	@Value("${ITEM_PARAM_URL}")
	private String ITEM_PARAM_URL;
	
	@Override
	public ItemInfo getItemById(long itemId) {
		
		try {
			//调用rest服务查询商品基本信息
			String result = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
			if(!StringUtils.isBlank(result)) {
				TaotaoResult taotaoResult = TaotaoResult.formatToPojo(result, ItemInfo.class);
				if(taotaoResult.getStatus() == 200) {
					ItemInfo item = (ItemInfo)taotaoResult.getData();
					return item;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getItemDescById(long itemId) {
		 
		try {
			//查询商品描述
			String result = HttpClientUtil.doGet(REST_BASE_URL + ITEM_DESC_URL + itemId) ;
			//转换成java对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(result, TbItemDesc.class);
			if(taotaoResult.getStatus() == 200) {
				TbItemDesc itemDesc = (TbItemDesc)taotaoResult.getData();
				//取商品描述信息
				String Descresult = itemDesc.getItemDesc();
				return Descresult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 *  规格参数查询
	 */
	@Override
	public String getItemParamById(long itemId) {
		try {
			String result = HttpClientUtil.doGet(REST_BASE_URL + ITEM_PARAM_URL + itemId);
			//把json转换成java对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(result, TbItemParamItem.class);
			if(taotaoResult.getStatus() == 200) {
				TbItemParamItem itemParamItem = (TbItemParamItem)taotaoResult.getData();
				String paramData = itemParamItem.getParamData();
				//生成html
				// 把规格参数json数据转换成java对象
				List<Map> jsonList = JsonUtils.jsonToList(paramData, Map.class);
				StringBuffer sb = new StringBuffer();
				sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
				sb.append("    <tbody>\n");
				for(Map m1:jsonList) {
					sb.append("        <tr>\n");
					sb.append("            <th class=\"tdTitle\" colspan=\"2\">"+m1.get("group")+"</th>\n");
					sb.append("        </tr>\n");
					List<Map> list2 = (List<Map>) m1.get("params");
					for(Map m2:list2) {
						sb.append("        <tr>\n");
						sb.append("            <td class=\"tdTitle\">"+m2.get("k")+"</td>\n");
						sb.append("            <td>"+m2.get("v")+"</td>\n");
						sb.append("        </tr>\n");
					}
				}
				sb.append("    </tbody>\n");
				sb.append("</table>");
				
				//返回Html片段
				return sb.toString();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

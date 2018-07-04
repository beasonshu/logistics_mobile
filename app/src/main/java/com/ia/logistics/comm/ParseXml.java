package com.ia.logistics.comm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ia.logistics.exception.XMLException;

public class ParseXml {

	public static final String ROOT_NODE = "b3pl";
	public static final String HEAD_NODE = "head";
	public static final String TRANFUNC_NODE = "tranfunc";
	public static final String BODY_NODE = "body";
	// public static final String OBJS_NODE = "objs";
	// public static final String OBJ_NODE = "obj";
	public static final String LINES_NODE = "line";
	public static final String Attribute_NAME = "name";
	public static final String Attribute_VALUE = "value";
	public static final String FROMID_NODE = "fromid";
	public static final String TOID_NODE = "toid";

	public static String createXMLFile(String tranCode, List value,
									   String fromId, String toId) throws XMLException {

		List[] values = new List[1];
		values[0] = value;

		return createXMLFile(tranCode, values, fromId, toId);
	}

	/**
	 * 创建XML文件
	 *
	 * @param tranCode
	 * @param values
	 *            数组里面放的是Map,map的key代表表名，value代表model对象
	 * @return
	 */
	public static String createXMLFile(String tranCode, List[] values,
									   String fromId, String toId) throws XMLException {
		try {
			// 使用DocumentHelper.createDocument方法建立一个文档实例
			Document document = DocumentHelper.createDocument();
			// 使用addElement方法方法创建根元素
			Element rootElement = document.addElement(ParseXml.ROOT_NODE);

			Element bobyElement = rootElement.addElement(BODY_NODE);

			for (int i = 0; i < values.length; i++) {

				List objs = (List) values[i];
				Element lineElement = bobyElement.addElement(LINES_NODE);

				Element objsElement = null;
				CommSet.d("baosight", objs.size()+"");
				for (int j = 0; j < objs.size(); j++) {

					Map map = (Map) objs.get(j);
					Set set = map.entrySet();
					Iterator iter = set.iterator();
					while (iter.hasNext()) {
						Map.Entry en = (Map.Entry) iter.next();
						String name = (String) en.getKey();

						// objsElement = bobyElement.addElement(OBJS_NODE);
						// objsElement.addAttribute(Attribute_NAME, name);

						objsElement = lineElement.addElement(name);

						Object obj = en.getValue();
						CommSet.d("baosight","name ==" + name);

						Field[] fields = obj.getClass().getDeclaredFields();
						if (fields != null && fields.length > 0) {
							for (int k = 0; k < fields.length; k++) {
								Field field = fields[k];
								if (field.getModifiers() != 2)
									continue;
								field.setAccessible(true);
								if (!"java.lang.String".equalsIgnoreCase(field
										.getType().getName()))
									continue;

								String value = (String) field.get(obj);

								Element objElement = objsElement
										.addElement(field.getName());
								// Element objElement = objsElement
								// .addElement(OBJ_NODE);
								// objElement.addAttribute(Attribute_NAME, field
								// .getName());
								// objElement.addAttribute(Attribute_VALUE,
								// StringUtil.trimToEmpty(value));

								objElement.setText(StringUtil.trimStr(value));

							}
						}
					}
				}
			}

			return document.asXML();
		} catch (Exception e) {
			e.printStackTrace();
			XMLException xmlEx = new XMLException("写XML文件出错，请检查！", e);
			throw xmlEx;
		}

	}

	public static Map parseDocument(String xmlStr) throws XMLException {
		Document document = null;
		Map map = new HashMap();
		try {
			document = DocumentHelper.parseText(xmlStr.trim());
			// 获取文档的根元素
			Element root = document.getRootElement();
			Element headEl = root.element(HEAD_NODE);
			Element tranfuncEl = headEl.element(TRANFUNC_NODE);
			String tranfunCode = tranfuncEl.getText();
			CommSet.d("baosight","tranfunCode==" + tranfunCode);
			Element fromIdEl = headEl.element(FROMID_NODE);
			String fromId = fromIdEl.getText();
			CommSet.d("baosight","fromId==" + fromId);
			Element toIdEl = headEl.element(TOID_NODE);
			String toId = toIdEl.getText();
			CommSet.d("baosight","toId==" + toId);
			map.put(TRANFUNC_NODE, tranfunCode);
			map.put(FROMID_NODE, fromId);
			map.put(TOID_NODE, toId);

		} catch (Exception e) {
			e.printStackTrace();
			XMLException xmlEx = new XMLException("解析XML出错，请检查！", e);
			throw xmlEx;
		}
		return map;
	}

	public static Object parseDocumentSBObj(String xmlStr, Object model)
			throws Exception {
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlStr);
			// 获取文档的根元素
			Element root = document.getRootElement();
			Element bodyEl = root.element(BODY_NODE);

			for (Iterator<?> i_pe = bodyEl.elementIterator(); i_pe.hasNext();) {
				Element e_line = (Element) i_pe.next();
				for (Iterator<?> i_objs = e_line.elementIterator(); i_objs
						.hasNext();) {

					Element e_obj = (Element) i_objs.next();

					for (Iterator<?> j_obj = e_obj.elementIterator(); j_obj
							.hasNext();) {

						Element attribute = (Element) j_obj.next();

						String objName = attribute.getName();
						String objValue = attribute.getText();

						// String objName =
						// e_obj.attributeValue(Attribute_NAME);
						// String objValue =
						// e_obj.attributeValue(Attribute_VALUE);
						CommSet.d("baosight","objName==" + objName + ",value="
								+ objValue);
						Field field = model.getClass()
								.getDeclaredField(objName);
						field.setAccessible(true);
						field.set(model, objValue);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return model;
	}

	public static List parseDocumentSBObjs(String xmlStr, Object model)
			throws Exception {
		Document document = null;
		List list = new ArrayList();
		try {
			document = DocumentHelper.parseText(xmlStr.trim());
			// 获取文档的根元素
			Element root = document.getRootElement();
			Element bodyEl = root.element(BODY_NODE);

			for (Iterator i_pe = bodyEl.elementIterator(); i_pe.hasNext();) {
				Element e_line = (Element) i_pe.next();

				for (Iterator i_objs = e_line.elementIterator(); i_objs
						.hasNext();) {
					Object objInstance = model.getClass().newInstance();
					Element e_obj = (Element) i_objs.next();

					for (Iterator j_obj = e_obj.elementIterator(); j_obj
							.hasNext();) {

						Element attribute = (Element) j_obj.next();

						String objName = attribute.getName();
						String objValue = attribute.getText();

						// String objName =
						// e_obj.attributeValue(Attribute_NAME);
						// String objValue =
						// e_obj.attributeValue(Attribute_VALUE);
						CommSet.d("baosight","objName==" + objName + ",value="
								+ objValue);
						Field field = objInstance.getClass().getDeclaredField(
								objName);
						field.setAccessible(true);
						field.set(objInstance, objValue);

					}
					list.add(objInstance);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}
//
//	public static TransBillModel parseDocumentSBChildObj(String xmlStr, TransBillModel model)throws Exception {
//		Document document = null;
//		try {
//			document = DocumentHelper.parseText(xmlStr.trim());
//			// 获取文档的根元素
//			Element root = document.getRootElement();
//			Element bodyEl = root.element(BODY_NODE);
//
//			for (Iterator i_pe = bodyEl.elementIterator(); i_pe.hasNext();) {
//				Element e_line = (Element) i_pe.next();
//				for (Iterator i_objs = e_line.elementIterator(); i_objs.hasNext();) {
//					Element e_objs = (Element) i_objs.next();
//
//					String objsName = e_objs.getName();
//					// String objsName = e_objs.attributeValue(Attribute_NAME);
//
//					System.out.println("objsName==" + objsName);
//					if (TransBillModel.Node_Name.equalsIgnoreCase(objsName)) {
//						for (Iterator i_obj = e_objs.elementIterator(); i_obj.hasNext();) {
//							Element attribute = (Element) i_obj.next();
//							String objName = attribute.getName();
//							String objValue = attribute.getText();
//							System.out.println("objName==" + objName + ",value=" + objValue);
//							Field field = model.getClass().getDeclaredField(objName);
//							field.setAccessible(true);
//							field.set(model, objValue);
//						}
//					}
//					if (TransBillChildModel.Node_Name.equalsIgnoreCase(objsName)) {
//						TransBillChildModel childModel = new TransBillChildModel();
//						for (Iterator i_obj = e_objs.elementIterator(); i_obj.hasNext();) {
//							Element attribute = (Element) i_obj.next();
//							String objName = attribute.getName();
//							String objValue = attribute.getText();
//							System.out.println("objName==" + objName + ",value=" + objValue);
//							Field field = childModel.getClass().getDeclaredField(objName);
//							field.setAccessible(true);
//							field.set(childModel, objValue);
//							model.getTransBillChildModels().add(childModel);
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//		return model;
//	}

	/** */
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			/**
			 * xml to model
			 */
//			xml2Model();

			/**
			 * model to xml
			 */
//			model2Xml();

			/**
			 * models to xml
			 */
//			models2Xml();

			/**
			 * xml to models
			 */
//			xml2Models();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String strChangeToXML(String str) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new ByteArrayInputStream(str.getBytes()));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		StringWriter writer = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("GBK");
		XMLWriter xmlwriter = new XMLWriter(writer, format);
		try {
			xmlwriter.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	//<b3pl><body><line><gps>
	//<cch></cch>
	//<cph>鄂A4567</cph>
	//<cysdm>004678</cysdm>
	//<jd>121.537955</jd>
	//<padid>861753008004329</padid>
	//<sjdm>U07204_034</sjdm>
	//<sjxm>陈世伟</sjxm>
	//<wd>31.22812</wd>
	//<zzzt>10</zzzt>
	//</gps></line></body></b3pl>

	/*public static List<GPSInfoSearchModel> pullXml(String sendXml) throws Exception{
		List<GPSInfoSearchModel> gpsInfoModel=null;
		GPSInfoSearchModel gpsInfo=null;
		XmlPullParser parser=Xml.newPullParser();
		parser.set
		int eventType = parser.getEventType();
		while(eventType !=XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if("b3pl".equals(parser.getName())){
					if("body".equals(parser.getName())){
						if("line".equals(parser.getName())){
							if("gps".equals(parser.getName())){
								gpsInfoModel =new ArrayList<GPSInfoSearchModel>();
							}else if("cch".equals(parser.getName())){
								gpsInfo = new GPSInfoSearchModel();
								String cch=parser.nextText();
								gpsInfo.setCch(cch);
							}else if("cph".equals(parser.getName())){
								String cph=parser.nextText();
							    gpsInfo.setCph(cph);
							}else if("cysdm".equals(parser.getName())){
								String cysdm=parser.nextText();
							    gpsInfo.setCysdm(cysdm);
							}else if("jd".equals(parser.getName())){
								String jd=parser.nextText();
								gpsInfo.setJd(jd);
							}else if("padid".equals(parser.getName())){
								String padid=parser.nextText();
							    gpsInfo.setCysdm(padid);
							}else if("sjdm".equals(parser.getName())){
								String sjdm=parser.nextText();
							    gpsInfo.setCysdm(sjdm);
							}else if("sjxm".equals(parser.getName())){
								String sjxm=parser.nextText();
							    gpsInfo.setCysdm(sjxm);
							}else if("wd".equals(parser.getName())){
								String wd=parser.nextText();
							    gpsInfo.setCysdm(wd);
							}else if("zzzt".equals(parser.getName())){
								String zzzt=parser.nextText();
							    gpsInfo.setCysdm(zzzt);
							}
						}
					}
				}
				break;
				case XmlPullParser.END_TAG:
					if("gps".equals(parser.getName())){
						gpsInfoModel.add(gpsInfo);
					}
					break;

			default:
				break;
			}
		}
		return gpsInfoModel;

	}*/

}

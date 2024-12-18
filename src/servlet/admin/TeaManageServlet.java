package servlet.admin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import bean.Tea;
import  bean.Catalog;
import  bean.PageBean;
import  bean.UpLoadImg;
import dao.TeaDao;
import  dao.CatalogDao;
import  dao.UpLoadImgDao;
import dao.impl.TeaDaoImpl;
import  dao.impl.CatalogDaoImpl;
import  dao.impl.UpLoadImgDaoImpl;
import  util.RanUtil;

import net.sf.json.JSONObject;


@WebServlet("/jsp/admin/TeaManageServlet")
public class TeaManageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String TEALIST_PATH = "teaManage/teaList.jsp";// 商品列表页面地址
	private static final String TEAADD_PATH = "teaManage/teaAdd.jsp";// 商品增加页面地址
	private static final String TEAEDIT_PATH = "teaManage/teaEdit.jsp";// 商品修改页面地址
	private static final String TEADETAIL_PATH = "teaManage/teaDetail.jsp";// 商品详情页面地址
	private static final String TEAIMGDIR_PATH="images/tea/teaimg/";//商品图片保存文件夹相对路径


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		switch (action) {
		case "list":
			teaList(request, response);
			break;
		case "detail":
			teaDetail(request, response);
			break;
		case "addReq":
			teaAddReq(request, response);
			break;
		case "add":
			teaAdd(request, response);
			break;
		case "edit":
			teaEdit(request, response);
			break;
		case "update":
			teaUpdate(request,response);
			break;
		case "find":
			teaFind(request, response);
			break;
		case "updateImg":
			updateImg(request,response);
			break;
		case "del":
			teaDel(request,response);
			break;
		case "batDel":
			teaBatDel(request,response);
			break;
		case "seach":
			seachTea(request,response);
		}
	}

	//搜索商品，且把搜索的结果分页显示
	private void seachTea(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		int maxSize = Integer.parseInt(request.getServletContext().getInitParameter("maxPageSize"));
		String teaname = request.getParameter("teaname");
		TeaDao bd = new TeaDaoImpl();
		PageBean pb = null;
		if(teaname != null && teaname != "") {
			pb = new PageBean(curPage, maxSize, bd.teaReadCount(teaname));
			request.setAttribute("teaList", bd.teaList(pb,teaname));
		}else {
			pb = new PageBean(curPage, maxSize, bd.teaReadCount());
			request.setAttribute("teaList", bd.teaList(pb));
		}
		
		request.setAttribute("pageBean", pb);
		request.getRequestDispatcher(TEALIST_PATH).forward(request, response);
	}

	//商品批量删除
	private void teaBatDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ids=request.getParameter("ids");
		TeaDao bd=new TeaDaoImpl();
		UpLoadImgDao uid=new UpLoadImgDaoImpl();
		File contextPath=new File(request.getServletContext().getRealPath("/"));
		
		String imgIds=bd.findimgIdByIds(ids);//批量查询图片的id并组成一组字符串
		
		List<UpLoadImg> list = uid.findImgByIds(imgIds);
		if(bd.teaBatDelById(ids)) {
			request.setAttribute("teaMessage", "商品已批量删除");
			if(uid.imgBatDelById(imgIds)) {
				for(UpLoadImg uli:list) {
					//批量删除本地文件
					File f=new File(contextPath,uli.getImgSrc());
					if(f.exists()) {
						f.delete();
					}
				}
			}
		}else {
			request.setAttribute("teaMessage", "商品批量删除失败");
		}
		//用户删除成功失败都跳转到用户列表页面
		teaList(request, response);//通过servlet中listUser跳到用户列表
	}

	//商品删除
	private void teaDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id=Integer.parseInt(request.getParameter("id"));
		File contextPath=new File(request.getServletContext().getRealPath("/"));
		TeaDao bd=new TeaDaoImpl();
		UpLoadImgDao uid=new UpLoadImgDaoImpl();
		Tea tea =bd.findTeaById(id);
		//这里先删除数据库商品信息，再删除商品图片及本地硬盘图片信息
		if(bd.teaDelById(id)) {
			request.setAttribute("teaMessage", "商品已删除");
			if(uid.imgDelById(tea.getImgId())) {
				//删除本地文件
				File f=new File(contextPath, tea.getUpLoadImg().getImgSrc());
				if(f.exists()) {
					f.delete();
				}
			}
		}else {
			request.setAttribute("teaMessage", "商品删除失败");
		}
		
		//用户删除成功失败都跳转到用户列表页面
		teaList(request, response);//通过servlet中listUser跳到用户列表
		
	}

	//商品更新
	private void teaUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TeaDao teaDao =new TeaDaoImpl();
		Tea tea =new Tea();
		// 获取并设置 teaId（假设 teaId 是必填项）
        String teaIdParam = request.getParameter("id");
        if (teaIdParam != null && !teaIdParam.trim().isEmpty()) {
            try {
                tea.setTeaId(Integer.parseInt(teaIdParam));
            } catch (NumberFormatException e) {
                // 处理无效的 teaId 格式
                System.out.println("Invalid teaId format: " + teaIdParam);
                return; // 或者抛出异常、返回错误信息等
            }
        } else {
            // 处理 teaId 为空的情况
            System.out.println("teaId is required and cannot be empty.");
            return; // 或者抛出异常、返回错误信息等
        }

        // 获取并设置 catalogId
        String catalogIdParam = request.getParameter("catalog");
        if (catalogIdParam != null && !catalogIdParam.trim().isEmpty()) {
            try {
                tea.setCatalogId(Integer.parseInt(catalogIdParam));
            } catch (NumberFormatException e) {
                // 处理无效的 catalogId 格式
                System.out.println("Invalid catalogId format: " + catalogIdParam);
            }
        }

        // 获取并设置 price
        String priceParam = request.getParameter("price");
        if (priceParam != null && !priceParam.trim().isEmpty()) {
            try {
                tea.setPrice(Double.parseDouble(priceParam));
            } catch (NumberFormatException e) {
                // 处理无效的 price 格式
                System.out.println("Invalid price format: " + priceParam);
            }
        }

        // 获取并设置 description
        String descriptionParam = request.getParameter("description");
        if (descriptionParam != null && !descriptionParam.trim().isEmpty()) {
            tea.setDescription(descriptionParam.trim());
        }

        // 获取并设置 recommend，将 String 转换为 boolean
        String recommendParam = request.getParameter("recommend");
        if (recommendParam != null && !recommendParam.trim().isEmpty()) {
            tea.setRecommend(Boolean.parseBoolean(recommendParam.trim()));
        }
		
		if(teaDao.teaUpdate(tea)) {
			request.setAttribute("teaMessage", "修改成功");
			teaList(request, response);
		}else {
			request.setAttribute("teaMessage", "图片失败");
			request.setAttribute("teaInfo", teaDao.findTeaById(tea.getTeaId()));
			request.getRequestDispatcher(TEAEDIT_PATH).forward(request, response);
		}
	}

	// 更新商品图片
	private void updateImg(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		int teaId = Integer.parseInt(request.getParameter("id"));
		boolean flag = false;
		String imgSrc = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		String imgName = null;
		String contentType = null;

		TeaDao teaDao = new TeaDaoImpl();
		UpLoadImgDao upImgDao = new UpLoadImgDaoImpl();
		
		File contextPath=new File(request.getServletContext().getRealPath("/"));
		File dirPath = new File( contextPath,TEAIMGDIR_PATH);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		DiskFileItemFactory dfif = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(dfif);
		List<FileItem> parseRequest = null;
		try {
			parseRequest = servletFileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Iterator<FileItem> iterator = parseRequest.iterator();
		while (iterator.hasNext()) {
			FileItem fileItem = iterator.next();
			if (!fileItem.isFormField()) {

				inputStream = fileItem.getInputStream();
				contentType = fileItem.getContentType();
				if ("image/jpeg".equals(contentType)) {
					imgName = RanUtil.getUUID() + ".jpg";
					flag = true;
				}
				if ("image/png".equals(contentType)) {
					imgName = RanUtil.getUUID() + ".png";
					flag = true;
				}

			}

		}
		if (flag) {
			imgSrc = TEAIMGDIR_PATH + imgName;
			outputStream = new FileOutputStream(new File(contextPath,imgSrc));
			IOUtils.copy(inputStream, outputStream);
			outputStream.close();
			inputStream.close();
			//根据商品id去查询图片信息
			Tea tea = teaDao.findTeaById(teaId);
			UpLoadImg upImg = tea.getUpLoadImg();
			// 删除旧图片文件如果存在
			File oldImg = new File(contextPath, tea.getUpLoadImg().getImgSrc());
			if (oldImg.exists()) {
				oldImg.delete();
			}
			upImg.setImgName(imgName);
			upImg.setImgSrc(imgSrc);
			upImg.setImgType(contentType);
			
			
			if (upImgDao.imgUpdate(upImg)) {
				request.setAttribute("teaMessage", "图片修改成功");
			} else {
				request.setAttribute("teaMessage", "图片修改失败");
			}
		} else {
			request.setAttribute("teaMessage", "图片修改失败");
		}
		teaEdit(request,response);
	}

	// 获取商品分类信息
	private void teaAddReq(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CatalogDao cd = new CatalogDaoImpl();
		request.setAttribute("catalog", cd.getCatalog());
		request.getRequestDispatcher(TEAADD_PATH).forward(request, response);

	}

	// 商品增加
	private void teaAdd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean flag = false;
		
		Map<String, String> map = new HashMap<>();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		File dirPath = new File(request.getServletContext().getRealPath("/") + TEAIMGDIR_PATH);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		DiskFileItemFactory dfif = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(dfif);
		// 解决乱码
		servletFileUpload.setHeaderEncoding("ISO8859_1");

		List<FileItem> parseRequest = null;
		try {
			parseRequest = servletFileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		Iterator<FileItem> iterator = parseRequest.iterator();

		while (iterator.hasNext()) {
			FileItem fileItem = iterator.next();
			// 判断是否是表单的普通字段true为普通表单字段，false为上传文件内容
			if (fileItem.isFormField()) {
				String name = new String(fileItem.getFieldName().getBytes("ISO8859_1"), "utf-8");
				String value = new String(fileItem.getString().getBytes("ISO8859_1"), "utf-8");
				map.put(name, value);
			} else {
				String imgName = null;

				String contentType = fileItem.getContentType();

				if ("image/jpeg".equals(contentType)) {
					imgName = RanUtil.getUUID() + ".jpg";
					flag = true;
				}
				if ("image/png".equals(contentType)) {
					imgName = RanUtil.getUUID() + ".png";
					flag = true;
				}
				if (flag) {
					inputStream = fileItem.getInputStream();
					File file = new File(dirPath, imgName);
					outputStream = new FileOutputStream(file);
					// 保存img信息到map集合中，后面传入对象使用
					map.put("imgName", imgName);
					map.put("imgSrc", TEAIMGDIR_PATH + imgName);
					map.put("imgType", contentType);
				}
			}
		}
		// 如果上传的内容小于3个必填项或者图片没有或类型不正确返回
		if (map.size() < 3 || !flag) {
			request.setAttribute("teaMessage", "商品添加失败");
			teaAddReq(request, response);
		} else {
			// 验证通过才可以保存图片流到本地
			IOUtils.copy(inputStream, outputStream);
			outputStream.close();
			inputStream.close();

			// 把map集合中存储的表单数据提取出来转换为tea对象
			// 这里要求商品增加的字段要和数据库字段一致，不然map集合转对象会出错
			Tea tea = new Tea();
			tea.setTeaName(map.get("teaName"));
			tea.setPrice(Double.parseDouble(map.get("price")));
			tea.setDescription(map.get("desc"));
			
			// 商品分类信息
			Catalog catalog = tea.getCatalog();
			catalog.setCatalogId(Integer.parseInt(map.get("catalog")));
			// 图片信息
			UpLoadImg upLoadImg = tea.getUpLoadImg();
			upLoadImg.setImgName(map.get("imgName"));
			upLoadImg.setImgSrc(map.get("imgSrc"));
			upLoadImg.setImgType(map.get("imgType"));

			// 增加商品先增加商品图片,商品图片增加成功了在添加商品信息
			UpLoadImgDao uid = new UpLoadImgDaoImpl();
			if (uid.imgAdd(tea.getUpLoadImg())) {
				// 获取商品图片添加后的id
				Integer imgId = uid.findIdByImgName(upLoadImg.getImgName());
				upLoadImg.setImgId(imgId);

				TeaDao bd = new TeaDaoImpl();
				if (bd.teaAdd(tea)) {
					request.setAttribute("teaMessage", "商品添加成功");
					teaList(request, response);
				} else {
					request.setAttribute("teaMessage", "商品添加失败");
					teaAddReq(request, response);
				}
			} else {
				// 图片添加失败就判定商品添加失败
				request.setAttribute("teaMessage", "商品添加失败");
				teaAddReq(request, response);
			}

		}
	}

	// 获取商品列表
	private void teaList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		int maxSize = Integer.parseInt(request.getServletContext().getInitParameter("maxPageSize"));
		TeaDao bd = new TeaDaoImpl();
		PageBean pb = new PageBean(curPage, maxSize, bd.teaReadCount());
		
		request.setAttribute("pageBean", pb);
		request.setAttribute("teaList", bd.teaList(pb));
		request.getRequestDispatcher(TEALIST_PATH).forward(request, response);
	}

	//商品详情页
	private void teaDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		TeaDao bd = new TeaDaoImpl();
		request.setAttribute("teaInfo", bd.findTeaById(Integer.parseInt(id)));
		request.getRequestDispatcher(TEADETAIL_PATH).forward(request, response);

	}

	//接收商品修改请求
	private void teaEdit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int teaId = Integer.parseInt(request.getParameter("id"));
		TeaDao teaDao = new TeaDaoImpl();
		CatalogDao catalogDao = new CatalogDaoImpl();
		request.setAttribute("catalog", catalogDao.getCatalog());//获取商品分类信息
		request.setAttribute("teaInfo", teaDao.findTeaById(teaId));//获取商品信息byId
		request.getRequestDispatcher(TEAEDIT_PATH).forward(request, response);
	}

	//ajax查找商品是否存在
	private void teaFind(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String teaName = request.getParameter("param");

		TeaDao bd = new TeaDaoImpl();
		//实例化json对象
		JSONObject json = new JSONObject();
		if (bd.findTeaByTeaName(teaName)) {
			json.put("info", "该商品已存在");
			json.put("status", "n");
		} else {
			json.put("info", "输入正确");
			json.put("status", "y");
		}
		response.getWriter().write(json.toString());
	}

}


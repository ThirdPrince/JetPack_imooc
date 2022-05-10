package com.jetpack_imooc.libnavcompiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.asm.FieldWriter;
import com.google.auto.service.AutoService;
import com.jetpack_imooc.libnavannotation.ActivityDestination;
import com.jetpack_imooc.libnavannotation.FragmentDestination;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;
import java.util.function.DoubleToIntFunction;
import java.util.logging.Filter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @author dhl
 * @version V1.0
 * @Title: NavProcessor
 * @Package $
 * @Description: 编译期间生成Destination.json
 * @date 2022 04 25
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.jetpack_imooc.libnavannotation.FragmentDestination", "com.jetpack_imooc.libnavannotation.ActivityDestination"})
public class NavProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private static final String OUTPUT_FILE_NAME = "destination.json";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
       // messager.printMessage(Diagnostic.Kind.ERROR, "init ");

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> fragmentElements = roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);
        Set<? extends Element> activityElements = roundEnvironment.getElementsAnnotatedWith(ActivityDestination.class);

        if (!fragmentElements.isEmpty() || !activityElements.isEmpty()) {
            HashMap<String, JSONObject> desMap = new HashMap<>();
            handleDestination(fragmentElements, FragmentDestination.class, desMap);
            handleDestination(activityElements, ActivityDestination.class, desMap);
            FileOutputStream fileOutputStream = null;
            OutputStreamWriter outputStreamWriter  = null;
            FileWriter fileWriter = null;
            //app/src/main/assets
            try {
                FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
                String resourcePath = resource.toUri().getPath();
                messager.printMessage(Diagnostic.Kind.NOTE, resourcePath);
                System.out.println(resourcePath);
                String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
                String assetsPath = appPath + "src/main/assets";
                File file = new File(assetsPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outPutFile = new File(file, OUTPUT_FILE_NAME);
                if (outPutFile.exists()) {
                    outPutFile.delete();
                }
                outPutFile.createNewFile();
                String content = JSON.toJSONString(desMap);
                System.out.println("content = "+content);
//                 fileWriter = new FileWriter(outPutFile);
//                fileWriter.write(content);
//                fileWriter.flush();
                fileOutputStream = new FileOutputStream(outPutFile);
                outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.write(content);
                outputStreamWriter.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
//                if(fileWriter != null){
//                    try {
//                        fileWriter.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                if(outputStreamWriter != null){
                    try {
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        return false;
    }

    private void handleDestination(Set<? extends Element> fragmentElements, Class<? extends Annotation> annotationClaz, HashMap<String, JSONObject> desMap) {
        for (Element element : fragmentElements) {

            TypeElement typeElement = (TypeElement) element;
            String pageUrl = null;
            String clazzName = typeElement.getQualifiedName().toString();
            int id = Math.abs(clazzName.hashCode());
            boolean needLogin = false;
            boolean asStarter = false;
            boolean isFragment = false;
            Annotation annotation = typeElement.getAnnotation(annotationClaz);
            if (annotation instanceof FragmentDestination) {
                FragmentDestination dest = (FragmentDestination) annotation;
                pageUrl = dest.pageUrl();
                needLogin = dest.needLogin();
                asStarter = dest.asStarter();
                isFragment = true;
            } else {
                ActivityDestination dest = (ActivityDestination) annotation;
                pageUrl = dest.pageUrl();
                needLogin = dest.needLogin();
                asStarter = dest.asStarter();
                isFragment = false;
            }

            if (desMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的配置URl className = " + clazzName);
            } else {
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("needLogin", needLogin);
                object.put("asStarter", asStarter);
                object.put("pageUrl", pageUrl);
                object.put("className", clazzName);
                object.put("isFragment", isFragment);
                desMap.put(pageUrl, object);
            }


        }

    }


}

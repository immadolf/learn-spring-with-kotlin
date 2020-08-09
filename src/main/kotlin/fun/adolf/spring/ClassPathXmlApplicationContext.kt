package `fun`.adolf.spring

import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.core.util.XmlUtil


/**
 *
 *
 * @author adolf
 * @date 2020/8/9
 * @since
 */
class ClassPathXmlApplicationContext(val locations: String) : ApplicationContext {

    private val idBeanContainer: MutableMap<String, Any> = mutableMapOf()
    private val classBeanContainer: MutableMap<Class<*>, Any> = mutableMapOf()
    private val aliasToId: MutableMap<String, String> = mutableMapOf()

    init {
        val resource = ClassPathResource(locations)
        val root = XmlUtil.readXML(resource.file)
        val elements = XmlUtil.getElements(root.documentElement, "bean")

        for (element in elements) {
            println("element = $element")
            var beanId: String? = element.getAttribute("id")
            val classStr: String? = element.getAttribute("class")

            if (classStr.isNullOrBlank()) {
                throw RuntimeException("need 'class' attribute.")
            }

            val beanClazz = Class.forName(classStr)
            val beanObj = beanClazz.newInstance()

            if (beanId.isNullOrBlank()) {
                beanId = beanClazz.simpleName.apply { this.replaceFirst(this[0], this[0].toLowerCase()) }
            }

            idBeanContainer[beanId!!] = beanObj
            classBeanContainer[beanClazz] = beanObj

            //如果存在别名，就将别名映射到id上
            element.getAttribute("name")?.let {
                it.split(",").forEach { alias ->
                    aliasToId[alias] = beanId
                }
            }
            //把id也当成别名
            aliasToId[beanId] = beanId
        }
    }

    override fun getBean(name: String): Any? {
        return idBeanContainer[aliasToId[name]]
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getBean(name: String, clazz: Class<T>): T? {
        return idBeanContainer[aliasToId[name]] as T?
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getBean(clazz: Class<T>): T? {
        return classBeanContainer[clazz] as T?
    }

    override fun getBeanDefinitionNames(): Array<String> {
        return idBeanContainer.keys.toTypedArray()
    }

    override fun getBeanNamesForType(clazz: Class<*>): Array<String> {
        return idBeanContainer.filter { entry -> entry.value.javaClass == clazz }
            .map { it.key }
            .toTypedArray()
    }

    override fun containsBeanDefinition(name: String): Boolean {
        return idBeanContainer.containsKey(aliasToId[name])
    }

    override fun containsBean(name: String): Boolean {
        return idBeanContainer.containsKey(aliasToId[name])
    }
}
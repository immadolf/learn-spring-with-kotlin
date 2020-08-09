package `fun`.adolf.spring

/**
 *
 *
 * @author adolf
 * @date 2020/8/9
 * @since
 */
interface ApplicationContext {
    fun getBean(name: String): Any?

    fun <T> getBean(name: String, clazz: Class<T>): T?

    fun <T> getBean(clazz: Class<T>): T?

    fun getBeanDefinitionNames(): Array<String>

    fun getBeanNamesForType(clazz: Class<*>): Array<String>

    fun containsBeanDefinition(name: String): Boolean

    fun containsBean(name: String): Boolean
}
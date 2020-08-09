import `fun`.adolf.entity.Person
import `fun`.adolf.spring.ApplicationContext
import `fun`.adolf.spring.ClassPathXmlApplicationContext
import org.junit.Assert
import org.junit.Before
import org.junit.Test


/**
 * IoC测试类.
 *
 * @author adolf
 * @date 2020/8/9
 * @since
 */
class TestIoC {

    lateinit var ctx: ApplicationContext

    @Before
    fun setUp() {
        ctx = ClassPathXmlApplicationContext("applicationContext.xml")
    }

    /**
     * 测试获取bean.
     */
    @Test
    fun test1() {

        val person: Person? = ctx.getBean("person") as Person
        Assert.assertNotNull(person)
    }

    @Test
    fun test2() {

        //通过这种⽅式获得对象，就不需要强制类型转换
        val person: Person? = ctx.getBean("person", Person::class.java)
        Assert.assertNotNull(person)
        println("person = $person")
    }

    @Test
    fun test3() {
        //当前Spring的配置⽂件中 只能有⼀个<bean class是Person类型
        val person: Person? = ctx.getBean(Person::class.java)
        Assert.assertNotNull(person)
        println("person = $person")
    }


    @Test
    fun test4() {
        //获取的是 Spring⼯⼚配置⽂件中所有bean标签的id值 person person1
        val beanDefinitionNames: Array<String> = ctx.getBeanDefinitionNames()
        for (beanDefinitionName in beanDefinitionNames) {
            println("beanDefinitionName = $beanDefinitionName")
        }
        Assert.assertNotNull(beanDefinitionNames)
    }

    @Test
    fun test5() {
        //根据类型获得Spring配置⽂件中对应的id值

        //根据类型获得Spring配置⽂件中对应的id值
        val beanNamesForType: Array<String> = ctx.getBeanNamesForType(Person::class.java)
        for (id in beanNamesForType) {
            println("id = $id")
        }
        Assert.assertNotNull(beanNamesForType)
    }

    @Test
    fun test6() {
        //⽤于判断是否存在指定id值的BeanDefinition
        Assert.assertFalse(ctx.containsBean("a"))
    }

    @Test
    fun test7() {
        //⽤于判断是否存在指定id值的bean
        Assert.assertTrue(ctx.containsBean("person"))
    }

    //    <bean id="person" name="p" class="fun.adolf.entity.Person"/>  定义别名为p
    @Test
    fun test8() {
        //用于测试别名
        Assert.assertTrue(ctx.containsBeanDefinition("p"))
        Assert.assertTrue(ctx.containsBean("p"))
    }
}
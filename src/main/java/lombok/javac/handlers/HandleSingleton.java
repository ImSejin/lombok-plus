package lombok.javac.handlers;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.javac.Javac8BasedLombokOptions;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.ResolutionResetNeeded;
import lombok.Singleton;
import org.kohsuke.MetaInfServices;

/**
 * @see <a href="https://www.baeldung.com/lombok-custom-annotation">Implementing a Custom Lombok Annotation</a>
 */
@MetaInfServices(JavacAnnotationHandler.class)
@HandlerPriority(0)
@ResolutionResetNeeded
public class HandleSingleton extends JavacAnnotationHandler<Singleton> {

    @Override
    public void handle(AnnotationValues<Singleton> annotation, JCAnnotation ast, JavacNode annotationNode) {
        Context context = annotationNode.getContext();
        Javac8BasedLombokOptions options = Javac8BasedLombokOptions.replaceWithDelombokOptions(context);
        options.deleteLombokAnnotations();
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, Singleton.class);
        JavacHandlerUtil.deleteImportFromCompilationUnit(annotationNode, "lombok.AccessLevel");
        JavacNode singletonClass = annotationNode.up();
        JavacTreeMaker singletonClassTreeMaker = singletonClass.getTreeMaker();
        addPrivateConstructor(singletonClass, singletonClassTreeMaker);

        JavacNode holderInnerClass = addInnerClass(singletonClass, singletonClassTreeMaker);
        addInstanceVar(singletonClass, singletonClassTreeMaker, holderInnerClass);
        addFactoryMethod(singletonClass, singletonClassTreeMaker, holderInnerClass);
    }

    private static void addPrivateConstructor(JavacNode singletonClass, JavacTreeMaker singletonTM) {
        JCTree.JCModifiers modifiers = singletonTM.Modifiers(Flags.PRIVATE);
        JCTree.JCBlock block = singletonTM.Block(0L, List.nil());
        JCTree.JCMethodDecl constructor = singletonTM.MethodDef(modifiers, singletonClass.toName("<init>"), null,
                List.nil(), List.nil(), List.nil(), block, null);

        JavacHandlerUtil.injectMethod(singletonClass, constructor);
    }

    private static JavacNode addInnerClass(JavacNode singletonClass, JavacTreeMaker singletonTM) {
        JCTree.JCModifiers modifiers = singletonTM.Modifiers(Flags.PRIVATE | Flags.STATIC);
        String innerClassName = singletonClass.getName() + "SingletonLazyHolder";
        JCTree.JCClassDecl innerClassDecl = singletonTM.ClassDef(modifiers, singletonClass.toName(innerClassName),
                List.nil(), null, List.nil(), List.nil());
        return JavacHandlerUtil.injectType(singletonClass, innerClassDecl);
    }

    private static void addInstanceVar(JavacNode singletonClass, JavacTreeMaker singletonClassTM,
                                       JavacNode holderClass) {
        JCTree.JCModifiers fieldMod = singletonClassTM.Modifiers(Flags.PRIVATE | Flags.STATIC | Flags.FINAL);

        JCTree.JCClassDecl singletonClassDecl = (JCTree.JCClassDecl) singletonClass.get();
        JCTree.JCIdent singletonClassType = singletonClassTM.Ident(singletonClassDecl.name);

        JCTree.JCNewClass newKeyword = singletonClassTM.NewClass(null, List.nil(), singletonClassType, List.nil(),
                null);

        JCTree.JCVariableDecl instanceVar = singletonClassTM.VarDef(fieldMod, singletonClass.toName("INSTANCE"),
                singletonClassType, newKeyword);
        JavacHandlerUtil.injectField(holderClass, instanceVar);
    }

    private static void addFactoryMethod(JavacNode singletonClass, JavacTreeMaker singletonClassTreeMaker,
                                         JavacNode holderInnerClass) {
        JCTree.JCModifiers modifiers = singletonClassTreeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC);

        JCTree.JCClassDecl singletonClassDecl = (JCTree.JCClassDecl) singletonClass.get();
        JCTree.JCIdent singletonClassType = singletonClassTreeMaker.Ident(singletonClassDecl.name);

        JCTree.JCBlock block = addReturnBlock(singletonClassTreeMaker, holderInnerClass);

        JCTree.JCMethodDecl factoryMethod = singletonClassTreeMaker.MethodDef(modifiers,
                singletonClass.toName("getInstance"), singletonClassType, List.nil(), List.nil(), List.nil(), block,
                null);
        JavacHandlerUtil.injectMethod(singletonClass, factoryMethod);
    }

    private static JCTree.JCBlock addReturnBlock(JavacTreeMaker singletonClassTreeMaker, JavacNode holderInnerClass) {
        JCTree.JCClassDecl holderInnerClassDecl = (JCTree.JCClassDecl) holderInnerClass.get();
        JavacTreeMaker holderInnerClassTreeMaker = holderInnerClass.getTreeMaker();
        JCTree.JCIdent holderInnerClassType = holderInnerClassTreeMaker.Ident(holderInnerClassDecl.name);

        JCTree.JCFieldAccess instanceVarAccess = holderInnerClassTreeMaker.Select(holderInnerClassType,
                holderInnerClass.toName("INSTANCE"));
        JCTree.JCReturn returnValue = singletonClassTreeMaker.Return(instanceVarAccess);

        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(returnValue);

        return singletonClassTreeMaker.Block(0L, statements.toList());
    }

}

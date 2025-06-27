package de.monticore.lang.sd4components._ast;

import de.monticore.ast.Comment;
import de.monticore.expressions.expressionsbasis._ast.ASTArguments;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.literals.mcliteralsbasis._symboltable.IMCLiteralsBasisScope;
import de.monticore.mcbasics._symboltable.IMCBasicsScope;
import de.monticore.symbols.basicsymbols._symboltable.IBasicSymbolsScope;
import de.monticore.symbols.compsymbols._ast.ASTSubcomponentArgument;
import de.monticore.symbols.compsymbols._symboltable.ICompSymbolsScope;
import de.monticore.symbols.compsymbols._visitor.CompSymbolsTraverser;
import de.se_rwth.commons.SourcePosition;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Expression2SubcomponentArgumentAdapter implements ASTSubcomponentArgument {

  public Expression2SubcomponentArgumentAdapter(ASTExpression astArguments) {
    adaptee = astArguments;
  }

  ASTExpression adaptee;

  @Override
  public boolean deepEquals(Object o) {
    return adaptee.deepEquals(o);
  }

  @Override
  public boolean deepEquals(Object o, boolean b) {
    return adaptee.deepEquals(o, b);
  }

  @Override
  public boolean deepEqualsWithComments(Object o) {
    return adaptee.deepEqualsWithComments(o);
  }

  @Override
  public boolean deepEqualsWithComments(Object o, boolean b) {
    return adaptee.deepEqualsWithComments(o, b);
  }

  @Override
  public boolean equalAttributes(Object o) {
    return adaptee.equalAttributes(o);
  }

  @Override
  public boolean equalsWithComments(Object o) {
    return adaptee.equalsWithComments(o);
  }

  @Override
  public ASTSubcomponentArgument deepClone() {
    return new Expression2SubcomponentArgumentAdapter(adaptee.deepClone());
  }

  @Override
  public SourcePosition get_SourcePositionEnd() {
    return adaptee.get_SourcePositionEnd();
  }

  @Override
  public void set_SourcePositionEnd(SourcePosition sourcePosition) {
    adaptee.set_SourcePositionEnd(sourcePosition);
  }

  @Override
  public void set_SourcePositionEndAbsent() {
    adaptee.set_SourcePositionEndAbsent();
  }

  @Override
  public boolean isPresent_SourcePositionEnd() {
    return adaptee.isPresent_SourcePositionEnd();
  }

  @Override
  public SourcePosition get_SourcePositionStart() {
    return adaptee.get_SourcePositionStart();
  }

  @Override
  public void set_SourcePositionStart(SourcePosition sourcePosition) {
    adaptee.set_SourcePositionStart(sourcePosition);
  }

  @Override
  public void set_SourcePositionStartAbsent() {
    adaptee.set_SourcePositionStartAbsent();
  }

  @Override
  public boolean isPresent_SourcePositionStart() {
    return adaptee.isPresent_SourcePositionStart();
  }

  @Override
  public void clear_PreComments() {
    adaptee.clear_PreComments();
  }

  @Override
  public boolean add_PreComment(Comment comment) {
    return adaptee.add_PreComment(comment);
  }

  @Override
  public boolean addAll_PreComments(Collection<Comment> collection) {
    return adaptee.addAll_PreComments(collection);
  }

  @Override
  public boolean contains_PreComment(Object o) {
    return adaptee.contains_PreComment(o);
  }

  @Override
  public boolean containsAll_PreComments(Collection<?> collection) {
    return adaptee.containsAll_PreComments(collection);
  }

  @Override
  public boolean isEmpty_PreComments() {
    return adaptee.isEmpty_PreComments();
  }

  @Override
  public Iterator<Comment> iterator_PreComments() {
    return adaptee.iterator_PreComments();
  }

  @Override
  public boolean remove_PreComment(Object o) {
    return adaptee.remove_PreComment(o);
  }

  @Override
  public boolean removeAll_PreComments(Collection<?> collection) {
    return adaptee.removeAll_PreComments(collection);
  }

  @Override
  public boolean retainAll_PreComments(Collection<?> collection) {
    return adaptee.retainAll_PreComments(collection);
  }

  @Override
  public int size_PreComments() {
    return adaptee.size_PreComments();
  }

  @Override
  public Comment[] toArray_PreComments(Comment[] comments) {
    return adaptee.toArray_PreComments(comments);
  }

  @Override
  public boolean removeIf_PreComment(Predicate<? super Comment> predicate) {
    return adaptee.removeIf_PreComment(predicate);
  }

  @Override
  public Spliterator<Comment> spliterator_PreComments() {
    return adaptee.spliterator_PreComments();
  }

  @Override
  public Stream<Comment> stream_PreComments() {
    return adaptee.stream_PreComments();
  }

  @Override
  public Stream<Comment> parallelStream_PreComments() {
    return adaptee.parallelStream_PreComments();
  }

  @Override
  public void forEach_PreComments(Consumer<? super Comment> consumer) {
    adaptee.forEach_PreComments(consumer);
  }

  @Override
  public void add_PreComment(int i, Comment comment) {
    adaptee.add_PreComment(i, comment);
  }

  @Override
  public boolean addAll_PreComments(int i, Collection<Comment> collection) {
    return adaptee.addAll_PreComments(i, collection);
  }

  @Override
  public Comment get_PreComment(int i) {
    return adaptee.get_PreComment(i);
  }

  @Override
  public int indexOf_PreComment(Object o) {
    return adaptee.indexOf_PreComment(o);
  }

  @Override
  public int lastIndexOf_PreComment(Object o) {
    return adaptee.lastIndexOf_PreComment(o);
  }

  @Override
  public boolean equals_PreComments(Object o) {
    return adaptee.equals_PreComments(o);
  }

  @Override
  public int hashCode_PreComments() {
    return adaptee.hashCode_PreComments();
  }

  @Override
  public ListIterator<Comment> listIterator_PreComments() {
    return adaptee.listIterator_PreComments();
  }

  @Override
  public Comment remove_PreComment(int i) {
    return adaptee.remove_PreComment(i);
  }

  @Override
  public List<Comment> subList_PreComments(int i, int i1) {
    return adaptee.subList_PreComments(i, i1);
  }

  @Override
  public void replaceAll_PreComments(UnaryOperator<Comment> unaryOperator) {
    adaptee.replaceAll_PreComments(unaryOperator);
  }

  @Override
  public void sort_PreComments(Comparator<? super Comment> comparator) {
    adaptee.sort_PreComments(comparator);
  }

  @Override
  public void set_PreCommentList(List<Comment> list) {
    adaptee.set_PreCommentList(list);
  }

  @Override
  public List<Comment> get_PreCommentList() {
    return adaptee.get_PreCommentList();
  }

  @Override
  public ListIterator<Comment> listIterator_PreComments(int i) {
    return adaptee.listIterator_PreComments(i);
  }

  @Override
  public Comment set_PreComment(int i, Comment comment) {
    return adaptee.set_PreComment(i, comment);
  }

  @Override
  public Object[] toArray_PreComments() {
    return adaptee.toArray_PreComments();
  }

  @Override
  public void clear_PostComments() {
    adaptee.clear_PostComments();
  }

  @Override
  public boolean add_PostComment(Comment comment) {
    return adaptee.add_PostComment(comment);
  }

  @Override
  public boolean addAll_PostComments(Collection<Comment> collection) {
    return adaptee.addAll_PostComments(collection);
  }

  @Override
  public boolean contains_PostComment(Object o) {
    return adaptee.contains_PostComment(o);
  }

  @Override
  public boolean containsAll_PostComments(Collection<?> collection) {
    return adaptee.containsAll_PostComments(collection);
  }

  @Override
  public boolean isEmpty_PostComments() {
    return adaptee.isEmpty_PostComments();
  }

  @Override
  public Iterator<Comment> iterator_PostComments() {
    return adaptee.iterator_PostComments();
  }

  @Override
  public boolean remove_PostComment(Object o) {
    return adaptee.remove_PostComment(o);
  }

  @Override
  public boolean removeAll_PostComments(Collection<?> collection) {
    return adaptee.removeAll_PostComments(collection);
  }

  @Override
  public boolean retainAll_PostComments(Collection<?> collection) {
    return adaptee.retainAll_PostComments(collection);
  }

  @Override
  public int size_PostComments() {
    return adaptee.size_PostComments();
  }

  @Override
  public Comment[] toArray_PostComments(Comment[] comments) {
    return adaptee.toArray_PostComments(comments);
  }

  @Override
  public boolean removeIf_PostComment(Predicate<? super Comment> predicate) {
    return adaptee.removeIf_PostComment(predicate);
  }

  @Override
  public Spliterator<Comment> spliterator_PostComments() {
    return adaptee.spliterator_PostComments();
  }

  @Override
  public Stream<Comment> stream_PostComments() {
    return adaptee.stream_PostComments();
  }

  @Override
  public Stream<Comment> parallelStream_PostComments() {
    return adaptee.parallelStream_PostComments();
  }

  @Override
  public void forEach_PostComments(Consumer<? super Comment> consumer) {
    adaptee.forEach_PostComments(consumer);
  }

  @Override
  public void add_PostComment(int i, Comment comment) {
    adaptee.add_PostComment(i, comment);
  }

  @Override
  public boolean addAll_PostComments(int i, Collection<Comment> collection) {
    return adaptee.addAll_PostComments(i, collection);
  }

  @Override
  public Comment get_PostComment(int i) {
    return adaptee.get_PostComment(i);
  }

  @Override
  public int indexOf_PostComment(Object o) {
    return adaptee.indexOf_PostComment(o);
  }

  @Override
  public int lastIndexOf_PostComment(Object o) {
    return adaptee.lastIndexOf_PostComment(o);
  }

  @Override
  public boolean equals_PostComments(Object o) {
    return adaptee.equals_PostComments(o);
  }

  @Override
  public int hashCode_PostComments() {
    return adaptee.hashCode_PostComments();
  }

  @Override
  public ListIterator<Comment> listIterator_PostComments() {
    return adaptee.listIterator_PostComments();
  }

  @Override
  public Comment remove_PostComment(int i) {
    return adaptee.remove_PostComment(i);
  }

  @Override
  public List<Comment> subList_PostComments(int i, int i1) {
    return adaptee.subList_PostComments(i, i1);
  }

  @Override
  public void replaceAll_PostComments(UnaryOperator<Comment> unaryOperator) {
    adaptee.replaceAll_PostComments(unaryOperator);
  }

  @Override
  public void sort_PostComments(Comparator<? super Comment> comparator) {
    adaptee.sort_PostComments(comparator);
  }

  @Override
  public void set_PostCommentList(List<Comment> list) {
    adaptee.set_PostCommentList(list);
  }

  @Override
  public List<Comment> get_PostCommentList() {
    return adaptee.get_PostCommentList();
  }

  @Override
  public ListIterator<Comment> listIterator_PostComments(int i) {
    return adaptee.listIterator_PostComments(i);
  }

  @Override
  public Comment set_PostComment(int i, Comment comment) {
    return adaptee.set_PostComment(i, comment);
  }

  @Override
  public Object[] toArray_PostComments() {
    return adaptee.toArray_PostComments();
  }

  @Override
  public String getName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isPresentName() {
    return false;
  }

  @Override
  public void setName(String s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNameAbsent() {

  }

  @Override
  public ASTExpression getExpression() {
    return adaptee;
  }

  @Override
  public void setExpression(ASTExpression astExpression) {
    adaptee = astExpression;
  }

  @Override
  public ICompSymbolsScope getEnclosingScope() {
    return (ICompSymbolsScope) adaptee.getEnclosingScope();
  }

  @Override
  public void setEnclosingScope(ICompSymbolsScope iCompSymbolsScope) {
    adaptee.setEnclosingScope(iCompSymbolsScope);
  }

  @Override
  public void setEnclosingScope(IBasicSymbolsScope iBasicSymbolsScope) {
    adaptee.setEnclosingScope(iBasicSymbolsScope);
  }

  @Override
  public void setEnclosingScope(IExpressionsBasisScope iExpressionsBasisScope) {
    adaptee.setEnclosingScope(iExpressionsBasisScope);
  }

  @Override
  public void setEnclosingScope(IMCBasicsScope imcBasicsScope) {
    adaptee.setEnclosingScope(imcBasicsScope);
  }

  @Override
  public void setEnclosingScope(IMCLiteralsBasisScope imcLiteralsBasisScope) {
    adaptee.setEnclosingScope(imcLiteralsBasisScope);
  }

  @Override
  public void accept(CompSymbolsTraverser visitor) {
    adaptee.accept(visitor);
  }
}

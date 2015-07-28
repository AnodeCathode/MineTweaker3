/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IGetter;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialVirtualMember<E extends IPartialExpression<E>> extends AbstractPartialExpression<E>
{
	private final E target;

	private final String name;

	private IGetter<E> getter;
	private ISetter<E> setter;
	private final List<ICallable<E>> methods;

	public PartialVirtualMember(CodePosition position, IMethodScope<E> scope, E target, String name)
	{
		super(position, scope);

		this.target = target;
		this.name = name;
		this.methods = new ArrayList<ICallable<E>>();
	}

	private PartialVirtualMember(CodePosition position, IMethodScope<E> scope, PartialVirtualMember<E> original)
	{
		super(position, scope);

		this.target = original.target;
		this.name = original.name;
		this.methods = original.methods;
	}

	public E getTarget()
	{
		return target;
	}

	public String getName()
	{
		return name;
	}

	public void setGetter(IGetter<E> getter)
	{
		this.getter = getter;
	}

	public void setSetter(ISetter<E> setter)
	{
		this.setter = setter;
	}

	public void addMethod(IVirtualCallable<E> method)
	{
		methods.add(method.bind(target));
	}

	public boolean isEmpty()
	{
		return getter == null && setter == null && methods.isEmpty();
	}

	public PartialVirtualMember<E> makeVariant(CodePosition position, IMethodScope<E> scope)
	{
		return new PartialVirtualMember<E>(position, scope, this);
	}

	@Override
	public E eval()
	{
		if (getter == null)
			throw new UnsupportedOperationException("This member is not a property or not readable");

		return getter.compileGet(getPosition(), getScope()).eval();
	}

	@Override
	public E assign(CodePosition position, E other)
	{
		if (setter == null)
			throw new UnsupportedOperationException("This member is not a properly or not writeable");

		return setter.compile(position, getScope(), other);
	}

	@Override
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		if (getter == null)
			throw new UnsupportedOperationException("This member is not a property or not readable");

		return getter.compileGet(getPosition(), getScope()).getMember(position, name);
	}

	@Override
	public List<ICallable<E>> getMethods()
	{
		return methods;
	}

	@Override
	public IGenericType<E> getType()
	{
		if (getter == null)
			return null;

		return getter.getType();
	}

	@Override
	public IPartialExpression<E> via(SymbolicFunction<E> function)
	{
		return this;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		IAny ctTarget = target.getCompileTimeValue();
		if (ctTarget == null)
			return null;
		
		return ctTarget.memberGet(name);
	}
}
/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.condition;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;
import org.assertj.core.description.JoinDescription;
import org.assertj.core.util.VisibleForTesting;


/**
 * Join of two or more <code>{@link Condition}</code>s.
 * @param <T> the type of object this condition accepts.
 *
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 */
public abstract class Join<T> extends Condition<T> {

  @VisibleForTesting
  final Collection<Condition<? super T>> conditions;

  /**
   * Creates a new <code>{@link Join}</code>.
   * @param conditions the conditions to join.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   */
  @SafeVarargs
  protected Join(Condition<? super T>... conditions) {
    checkNotNull(conditions, conditionsIsNull());
    this.conditions = Arrays.stream(conditions).map(Join::notNull).collect(toList());
    List<Description> descriptions = this.conditions.stream()
                                                    .map(Condition::description)
                                                    .collect(toList());
    this.describedAs(new JoinDescription(descriptionPrefix() + ":[", "]", descriptions));
  }

  /**
   * method used to prefix the subclass join description, ex: "all of"
   * @return the prefix to use to build the description.
   */
  public abstract String descriptionPrefix();

  /**
   * Creates a new <code>{@link Join}</code>.
   * @param conditions the conditions to join.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   */
  protected Join(Iterable<? extends Condition<? super T>> conditions) {
    checkNotNull(conditions, conditionsIsNull());
    this.conditions = new ArrayList<>();
    for (Condition<? super T> condition : conditions)
      this.conditions.add(notNull(condition));
  }

  private static String conditionsIsNull() {
    return "The given conditions should not be null";
  }

  private static <T> Condition<T> notNull(Condition<T> condition) {
    return checkNotNull(condition, "The given conditions should not have null entries");
  }

  /**
   * Returns the conditions to join.
   * @return the conditions to join.
   */
  protected final Collection<Condition<? super T>> conditions() {
    return unmodifiableCollection(conditions);
  }
}

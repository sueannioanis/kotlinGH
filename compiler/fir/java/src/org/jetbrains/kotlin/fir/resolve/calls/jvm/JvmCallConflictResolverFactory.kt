/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.resolve.calls.jvm

import org.jetbrains.kotlin.fir.NoMutableState
import org.jetbrains.kotlin.fir.resolve.BodyResolveComponents
import org.jetbrains.kotlin.fir.resolve.calls.*
import org.jetbrains.kotlin.fir.resolve.inference.InferenceComponents
import org.jetbrains.kotlin.fir.types.typeContext
import org.jetbrains.kotlin.resolve.calls.results.TypeSpecificityComparator
import org.jetbrains.kotlin.resolve.jvm.JvmTypeSpecificityComparator

@NoMutableState
object JvmCallConflictResolverFactory : ConeCallConflictResolverFactory() {
    override fun create(
        typeSpecificityComparator: TypeSpecificityComparator,
        components: InferenceComponents,
        transformerComponents: BodyResolveComponents
    ): ConeCompositeConflictResolver {
        val specificityComparator = JvmTypeSpecificityComparator(components.session.typeContext)
        return ConeCompositeConflictResolver(
            ConeOverloadConflictResolver(specificityComparator, components, transformerComponents),
            ConeEquivalentCallConflictResolver(specificityComparator, components, transformerComponents),
            JvmPlatformOverloadsConflictResolver(specificityComparator, components, transformerComponents),
            ConeIntegerOperatorConflictResolver(specificityComparator, components, transformerComponents),
            FilteringOutOriginalInPresenceOfSmartCastConeCallConflictResolver
        )
    }
}

package gay.pizza.pkg.apk

class ApkIndexGraph(val index: ApkIndex) {
  private val mutableRequirementToProvides = mutableMapOf<ApkIndexRequirementRef, MutableSet<ApkIndexPackage>>()
  private val installIfsCache = mutableMapOf<ApkIndexRequirementRef, MutableSet<ApkIndexRequirementRef>>()

  val requirementToProvides: Map<ApkIndexRequirementRef, Set<ApkIndexPackage>>
    get() = mutableRequirementToProvides

  init {
    for (pkg in index.packages) {
      for (provide in pkg.provides) {
        mutableRequirementToProvides.getOrPut(provide) { mutableSetOf() }.add(pkg)
      }
      mutableRequirementToProvides.getOrPut(pkg) { mutableSetOf() }.add(pkg)
      for (installIfPkg in pkg.installIf) {
        installIfsCache.getOrPut(installIfPkg) { mutableSetOf() }.add(pkg)
      }
    }
  }

  fun validateSoundGraph() {
    for (pkg in index.packages) {
      for (dependency in pkg.dependencies) {
        val provides = requirementToProvides[dependency]
        if (provides.isNullOrEmpty()) {
          throw RuntimeException("Package ${pkg.id} has dependency ${dependency.id} that is not satisfied.")
        }
      }
    }
  }

  fun findInstallIfs(pkg: ApkIndexPackage): Set<ApkIndexRequirementRef> = installIfsCache[pkg] ?: emptySet()

  fun tree(pkg: ApkIndexPackage): ApkDependencyTree = ApkDependencyTree(this, pkg)
}

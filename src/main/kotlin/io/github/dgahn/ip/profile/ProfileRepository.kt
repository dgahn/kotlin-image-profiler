package io.github.dgahn.ip.profile

import javax.persistence.Persistence


class ProfileRepository {

    private val emf = Persistence.createEntityManagerFactory("image-profiler")

    fun save(profile: Profile?): Profile? {
        val em = emf.createEntityManager()
        val tx = em.transaction
        tx.begin()
        try {
            em.persist(profile)
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            em.close()
        }

        return profile
    }

    fun findProfileSummaryList(): List<ProfileSummaryDto>? {
        val em = emf.createEntityManager()
        val tx = em.transaction
        var profiles: List<ProfileSummaryDto>? = null
        tx.begin()
        try {
            profiles = em.createNativeQuery(
                "SELECT p.profile_id AS id, p.name AS name FROM Profile p", "ProfileSummaryMapping"
            ).resultList as List<ProfileSummaryDto>?
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            em.close()
        }

        return profiles
    }

    fun findById(id: Long): Profile? {
        val em = emf.createEntityManager()
        val tx = em.transaction
        tx.begin()

        var profiles: List<Profile?> = ArrayList()
        try {
            profiles = em.createQuery(
                "SELECT p " +
                        "FROM Profile p " +
                        "WHERE p.id =:id", Profile::class.java
            )
                .setParameter("id", id)
                .resultList
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            em.close()
        }

        return if (profiles.isEmpty()) null else profiles[0]
    }

    fun removeAll() {
        val em = emf.createEntityManager()
        val tx = em.transaction
        tx.begin()

        try {
            val result = em.createQuery("DELETE FROM Profile p").executeUpdate()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            em.close()
        }

    }

}
